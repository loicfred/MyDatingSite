package my.dating.app.controller;

import my.dating.app.object.Chat;
import my.dating.app.object.ChatMessage;
import my.dating.app.object.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static my.utilities.util.Utilities.StopString;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @GetMapping("/user/{chatID}")
    @ResponseBody
    @Cacheable(value = "chats", key = "#chatID")
    public Map<String, Object> openChat(@PathVariable Long chatID, Principal principal) {
        Chat.Latest_Chat LC = Chat.Latest_Chat.find(chatID, principal.getName());
        if (LC == null) throw new AccessDeniedException("Cannot access this chat");

        Map<String, Object> result = new HashMap<>();
        result.put("messages", LC.getMessages());
        if (LC.getMessages().stream().anyMatch(m -> !m.isRead())) LC.readAllMessages(LC.getPartnerID());

        Map<String, Object> partnerData = new HashMap<>();
        partnerData.put("Username", LC.getPartnerUsername());
        partnerData.put("Name", LC.getPartnerName());
        result.put("partner", partnerData);
        return result;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    @CacheEvict(value = "chats", key = "#message.chatID")
    public void sendMessage(ChatMessage message, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(message.ChatID, principal.getName());
        if (chat == null || !(chat.UserID1 == currentUser.ID || chat.UserID2 == currentUser.ID)) {
            throw new AccessDeniedException("You are not allowed to send messages in this chat");
        }

        message.setUserID(currentUser.ID);
        message.SaveElseWrite();

        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/receive", message);
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/receive", message);

        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/notification/send", Chat.Latest_Chat.find(message.ChatID, currentUser.getUsername()));
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/notification/send", Chat.Latest_Chat.find(message.ChatID, chat.getPartnerUsername()));
    }

    @MessageMapping("/edit")
    @CacheEvict(value = "chats", key = "#editedMessage.chatID")
    public void editMessage(ChatMessage editedMessage, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        String newMessage = editedMessage.getMessage();
        editedMessage = ChatMessage.getById(editedMessage.getID());
        if (editedMessage == null || editedMessage.getUserID() != currentUser.ID) {
            throw new AccessDeniedException("Cannot edit this message");
        }

        editedMessage.setMessage(StopString(newMessage, 1024));
        editedMessage.setEdited(true);
        editedMessage.setUpdatedAtTime(Instant.now().toEpochMilli());
        editedMessage.Save();

        Chat.Latest_Chat chat = Chat.Latest_Chat.find(editedMessage.getChatID(), principal.getName());
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/update", editedMessage);
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/update", editedMessage);

        if (chat.LatestMessageID == editedMessage.ID) {
            messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/notification/update", Chat.Latest_Chat.find(editedMessage.ChatID, currentUser.getUsername()));
            messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/notification/update", Chat.Latest_Chat.find(editedMessage.ChatID, chat.getPartnerUsername()));
        }
    }

    @MessageMapping("/delete")
    @CacheEvict(value = "chats", key = "#messageToDelete.chatID")
    public void deleteMessage(ChatMessage messageToDelete, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        messageToDelete = ChatMessage.getById(messageToDelete.getID());
        if (messageToDelete == null || messageToDelete.getUserID() != currentUser.ID) {
            throw new AccessDeniedException("Cannot delete this message");
        }

        messageToDelete.Delete();

        Chat.Latest_Chat chat = Chat.Latest_Chat.find(messageToDelete.getChatID(), principal.getName());
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/delete", messageToDelete.getID());
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/delete", messageToDelete.getID());

        if (chat.LatestMessageID == messageToDelete.ID) {
            messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/notification/update", Chat.Latest_Chat.find(messageToDelete.ChatID, currentUser.getUsername()));
            messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/notification/update", Chat.Latest_Chat.find(messageToDelete.ChatID, chat.getPartnerUsername()));
        }
    }

    @MessageMapping("/typing")
    public void sendTyping(Long chatId, Principal principal) {
        Chat.Latest_Chat chat = Chat.Latest_Chat.find(chatId, principal.getName());
        messagingTemplate.convertAndSendToUser(chat.getPartnerUsername(), "/queue/typing", "");
    }
}
