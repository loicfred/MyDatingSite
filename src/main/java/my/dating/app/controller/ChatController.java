package my.dating.app.controller;

import my.dating.app.object.Chat;
import my.dating.app.object.ChatMessage;
import my.dating.app.object.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @GetMapping("/{username}")
    public String openChat(@PathVariable String username, Model model) throws SQLException {
        model.addAttribute("isAnonymous", SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = User.getByUsername(auth.getName());
        User chatPartner = User.getByUsername(username);

        Chat chat = Chat.getWithUser(currentUser, chatPartner);

        model.addAttribute("chat", chat);
        model.addAttribute("Me", currentUser);
        model.addAttribute("Partner", chatPartner);
        model.addAttribute("messages", chat.getMessages());
        return "chat";
    }

    @GetMapping("/{chatId}/messages")
    @ResponseBody
    public List<ChatMessage> getMessages(@PathVariable Long chatId) {
        Chat chat = Chat.getById(chatId);
        return chat.getMessages();
    }

    @PostMapping("/{username}/send")
    public String sendMessage(@PathVariable String username, @RequestParam String content) throws SQLException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = User.getByUsername(auth.getName());
        User chatPartner = User.getByUsername(username);
        Chat chat = Chat.getWithUser(currentUser, chatPartner);
        if (content.isEmpty()) return "redirect:/chat/" + username;
        new ChatMessage(chat.ID, currentUser, content);
        return "redirect:/chat/" + username;
    }

    @PostMapping("/{username}/message/{msgId}/edit")
    public String editMessage(@PathVariable String username, @PathVariable Long msgId, @RequestParam String content) throws SQLException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = User.getByUsername(auth.getName());
        ChatMessage M = ChatMessage.getById(msgId);
        if (M.UserID == currentUser.ID) {
            M.setMessage(content);
            M.Save();
        }
        return "redirect:/chat/" + username;
    }

    @PostMapping("/{username}/message/{msgId}/delete")
    public String deleteMessage(@PathVariable String username, @PathVariable Long msgId) throws SQLException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = User.getByUsername(auth.getName());
        ChatMessage M = ChatMessage.getById(msgId);
        if (M.UserID == currentUser.ID) M.Delete();
        return "redirect:/chat/" + username;
    }
}
