package my.dating.app.controller;

import my.dating.app.object.Chat;
import my.dating.app.object.ChatMessage;
import my.dating.app.object.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static my.utilities.util.Utilities.StopString;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @GetMapping("/user/{chatId}")
    @ResponseBody
    public Map<String, Object> getChat(@PathVariable Long chatId, Principal principal) {
        Chat.Latest_Chat LC = Chat.Latest_Chat.find(chatId, principal.getName());
        if (LC == null) throw new AccessDeniedException("Cannot access this chat");


        Map<String, Object> result = new HashMap<>();
        result.put("messages", LC.getMessages());

        Map<String, Object> partnerData = new HashMap<>();
        partnerData.put("Username", LC.Username);
        partnerData.put("Name", LC.Name);
        result.put("partner", partnerData);
        return result;
    }


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send") // client sends to /app/send
    public void sendMessage(ChatMessage message, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        Chat chat = Chat.getById(message.ChatID);

        if (chat == null || !(chat.UserID1 == currentUser.ID || chat.UserID2 == currentUser.ID)) {
            throw new AccessDeniedException("You are not allowed to send messages in this chat");
        }

        message.setUserID(currentUser.ID);
        message.SaveElseWrite();

        // send only to participants
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/receive", message);
        messagingTemplate.convertAndSendToUser(chat.UserID1 == currentUser.ID ? User.getById(chat.UserID2).getUsername() : User.getById(chat.UserID1).getUsername(), "/queue/receive", message);
    }

    @MessageMapping("/edit")
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

        Chat chat = Chat.getById(editedMessage.getChatID());
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/update", editedMessage);
        messagingTemplate.convertAndSendToUser(chat.UserID1 == currentUser.ID ? User.getById(chat.UserID2).getUsername() : User.getById(chat.UserID1).getUsername(), "/queue/update", editedMessage);
    }

    @MessageMapping("/delete")
    public void deleteMessage(ChatMessage messageToDelete, Principal principal) throws Exception {
        User currentUser = User.getByUsername(principal.getName());
        messageToDelete = ChatMessage.getById(messageToDelete.getID());
        if (messageToDelete == null || messageToDelete.getUserID() != currentUser.ID) {
            throw new AccessDeniedException("Cannot delete this message");
        }

        messageToDelete.Delete(); // remove from DB

        Chat chat = Chat.getById(messageToDelete.getChatID());
        messagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/queue/delete", messageToDelete.getID());
        messagingTemplate.convertAndSendToUser(chat.UserID1 == currentUser.ID ? User.getById(chat.UserID2).getUsername() : User.getById(chat.UserID1).getUsername(), "/queue/delete", messageToDelete.getID());
    }
}
