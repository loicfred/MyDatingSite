package my.dating.app.controller;

import my.dating.app.object.Chat;
import my.dating.app.object.msg.Message_Reaction;
import my.dating.app.object.msg.attachment.Draft_Attachment;
import my.dating.app.object.msg.attachment.Message_Attachment;
import my.dating.app.object.msg.Draft;
import my.dating.app.object.msg.Message;
import my.dating.app.object.User;
import my.dating.app.object.msg.BaseMessage;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static my.utilities.util.Utilities.GenerateRandomNumber;
import static my.utilities.util.Utilities.StopString;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final CacheManager cacheManager;
    public ChatController(SimpMessagingTemplate messagingTemplate, CacheManager cacheManager) {
        this.messagingTemplate = messagingTemplate;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/user/{chatID}")
    @ResponseBody
    @Cacheable(value = "chats", key = "#chatID")
    public Map<String, Object> openChat(@PathVariable Long chatID, Principal principal) {
        Chat.Latest_Chat LC = Chat.Latest_Chat.find(chatID, principal.getName());
        if (LC == null) throw new AccessDeniedException("Cannot access this chat");

        Map<String, Object> result = new HashMap<>();
        List<Message.Message_View> messages = LC.getMessages();
        for (Message.Message_View message : messages) {
            if (message.getAttachmentCount() == 0)
                message.attachments = new ArrayList<>();
            if (message.getReactionCount() == 0)
                message.reactions = new ArrayList<>();
        }
        result.put("messages", LC.getMessages());
        if (LC.getMessages().stream().anyMatch(m -> !m.isRead())) LC.readAllMessages(LC.getPartnerID());

        Map<String, Object> partnerData = new HashMap<>();
        partnerData.put("Username", LC.getPartnerUsername());
        partnerData.put("Name", LC.getPartnerName());
        result.put("partner", partnerData);
        Draft D = Draft.get(chatID, LC.getMyId());
        if (D != null) D.getAttachments();
        result.put("draft", D);
        return result;
    }

    @MessageMapping("/typing")
    public void sendTyping(BaseMessage msg, Principal principal, SimpMessageHeaderAccessor socketSession) throws SQLException {
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(msg.ChatID, principal.getName());
        if (chat == null) return;
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/typing", msg.ChatID);
        if (GenerateRandomNumber(1,3) != 1) return;
        Map<String,Object> session = socketSession.getSessionAttributes();
        if (session != null) {
            if (session.get("UserID") == null) session.put("UserID", User.getByUsername(principal.getName()).ID);
            new Draft(msg.ChatID, (long) session.get("UserID"), StopString(msg.Content, 1024)).Write();
        }
    }

    @MessageMapping("/message/send")
    public void sendMessage(BaseMessage msg, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Draft draft = Draft.get(msg.ChatID, currentUser.ID);
        if (draft == null || (draft.getContent().isEmpty() && draft.getAttachments().isEmpty())) return;
        draft.Content = StopString(msg.Content, 1024);
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(draft.ChatID, principal.getName());
        if (chat == null || !(chat.UserID1 == currentUser.ID || chat.UserID2 == currentUser.ID)) {
            throw new AccessDeniedException("You are not allowed to send messages in this chat");
        }
        Message message = new Message(draft.ChatID, currentUser.ID, draft.getContent());
        for (Draft_Attachment dAttachment : draft.getAttachments()) {
            message.addAttachments(new Message_Attachment(dAttachment, message.ID));
        }
        draft.Delete();

        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/message/receive", message);
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/message/receive", message);
        evictChatCache(chat.ID);
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/notification/receive", Chat.Latest_Chat.find(message.ChatID, currentUser.getUsername()));
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/notification/receive", Chat.Latest_Chat.find(message.ChatID, chat.getPartnerUsername()));
    }

    @MessageMapping("/message/edit")
    public void editMessage(Message editedMessage, Principal principal) throws Exception {
        if (editedMessage.getContent().isEmpty()) return;
        User currentUser = User.getByUsername(principal.getName());
        String newMessage = editedMessage.getContent();
        editedMessage = Message.getById(editedMessage.getID());
        if (editedMessage == null || editedMessage.getUserID() != currentUser.ID) {
            throw new AccessDeniedException("Cannot edit this message");
        }

        editedMessage.setContent(StopString(newMessage, 1024));
        editedMessage.setUpdatedAtTime(Instant.now().toEpochMilli());
        editedMessage.setEdited(true);
        editedMessage.Update();

        Chat.Latest_Chat chat = Chat.Latest_Chat.find(editedMessage.getChatID(), principal.getName());
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/message/update", editedMessage);
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/message/update", editedMessage);
        updateNotification(editedMessage, currentUser, chat);
    }

    @MessageMapping("/message/delete")
    public void deleteMessage(@RequestParam Long messageId, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Message messageToDelete = Message.getById(messageId);
        if (messageToDelete == null || messageToDelete.getUserID() != currentUser.ID) {
            throw new AccessDeniedException("Cannot delete this message");
        }

        messageToDelete.Delete();
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(messageToDelete.getChatID(), principal.getName());
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/message/delete", messageToDelete.getID());
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/message/delete", messageToDelete.getID());
        updateNotification(messageToDelete, currentUser, chat);
    }

    @MessageMapping("/message/attachment/delete")
    public void deleteMessageFiles(@RequestParam Long attachmentId, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Message_Attachment attachment = Message_Attachment.getById(attachmentId, "ID", "MessageID");
        if (attachment == null) return;
        Message M = Message.getById(attachment.MessageID);
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(M.ChatID, principal.getName());
        if (chat == null || M.UserID != currentUser.ID) throw new AccessDeniedException("Cannot delete this attachment");
        attachment.Delete();
        if (M.Content.isEmpty() && M.getAttachments().isEmpty()) {
            M.Delete();
            messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/message/delete", M.getID());
            messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/message/delete", M.getID());
        } else {
            messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/attachment/delete", attachment);
            messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/attachment/delete", attachment);
        }
        evictChatCache(M.ChatID);
        evictAttachmentCache(attachmentId);
    }


    // made use of Ajax here because STOMP doesn't support files, and it involves communication between
    // only one client (partner doesn't care about your drafts)
    @PostMapping("/draft/attachment/upload")
    @ResponseBody
    public List<Draft_Attachment> uploadFiles(@RequestParam Long chatId, @RequestParam("attachments") List<MultipartFile> attachments, Principal principal) throws Exception {
        if (attachments.isEmpty()) return null;
        User currentUser = User.getByUsername(principal.getName());
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(chatId, principal.getName());
        if (chat == null || !(chat.UserID1 == currentUser.ID || chat.UserID2 == currentUser.ID)) {
            throw new AccessDeniedException("Cannot upload files in this chat");
        }

        Draft draft = new Draft(chatId, currentUser.ID, "").Write();
        draft.ClearAttachments();
        for (MultipartFile file : attachments)
            if (!file.isEmpty()) draft.addAttachments(new Draft_Attachment(draft.ID, file.getOriginalFilename(), file.getContentType(), file.getBytes()));
        evictChatCache(chat.ID);
        return draft.getAttachments();
    }
    @DeleteMapping("/draft/attachment/delete")
    @ResponseBody
    public Draft_Attachment deleteDraftFiles(@RequestParam Long attachmentId, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Draft_Attachment attachment = Draft_Attachment.getById(attachmentId);
        if (attachment == null) return null;
        Draft D = Draft.getById(attachment.MessageID);
        if (D.UserID != currentUser.ID) throw new AccessDeniedException("Cannot delete this attachment");
        evictChatCache(D.ChatID);
        evictAttachmentCache(attachmentId);
        attachment.Delete();
        return attachment;
    }



    @MessageMapping("/reaction/send")
    public void sendReaction(Message_Reaction react, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Message.Message_View M = Message.Message_View.getById(react.MessageID);
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(M.ChatID, principal.getName());
        if (chat == null) throw new AccessDeniedException("Cannot send reactions in this chat");
        if (M.ReactionCount >= 12) throw new AccessDeniedException("You cannot send more than 12 reactions in a message.");
        new Message_Reaction(currentUser.ID, M.ID, react.Emoji);
        Map<String, Object> result = new HashMap<>();
        result.put("MessageID", M.ID);
        result.put("reactions", Message_Reaction.Message_Reaction_View.getByMessage(M.ID));
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/reaction/refresh", result);
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/reaction/refresh", result);
        evictChatCache(chat.ID);
    }
    @MessageMapping("/reaction/delete")
    public void deleteReaction(Message_Reaction react, Principal principal) throws SQLException {
        User currentUser = User.getByUsername(principal.getName());
        Message_Reaction R = Message_Reaction.get(currentUser.ID, react.MessageID, react.Emoji);
        Message M = Message.getById(R.MessageID);
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(M.ChatID, principal.getName());
        if (chat == null) throw new AccessDeniedException("Cannot delete reactions in this chat");
        if (R.getUserID() != currentUser.getId()) throw new AccessDeniedException("Cannot delete reactions from this user");
        R.Delete();
        Map<String, Object> result = new HashMap<>();
        result.put("MessageID", M.ID);
        result.put("reactions", Message_Reaction.Message_Reaction_View.getByMessage(M.ID));
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/reaction/refresh", result);
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/reaction/refresh", result);
        evictChatCache(chat.ID);
    }

    private void updateNotification(Message editedMessage, User currentUser, Chat.Latest_Chat chat) {
        evictChatCache(chat.ID);
        if (chat.LatestMessageID.equals(editedMessage.ID)) {
            messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/notification/update", Chat.Latest_Chat.find(editedMessage.ChatID, currentUser.getUsername()));
            messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/notification/update", Chat.Latest_Chat.find(editedMessage.ChatID, chat.getPartnerUsername()));
        }
    }
    public void evictChatCache(Long chatId) {
        Cache cache = cacheManager.getCache("chats");
        if (cache != null) cache.evict(chatId);
    }
    public void evictAttachmentCache(Long attachmentId) {
        Cache cache = cacheManager.getCache("attachments");
        if (cache != null) cache.evict(attachmentId);
    }
}