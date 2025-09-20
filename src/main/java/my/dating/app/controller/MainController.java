package my.dating.app.controller;

import my.dating.app.object.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String defaultPage(Model model, Principal loggedUser) {
        model.addAttribute("principal", loggedUser);
        return "home";
    }
    @GetMapping("/home")
    public String home(Model model, Principal loggedUser) {
        return defaultPage(model, loggedUser);
    }


    @GetMapping("/chat")
    public String openChat(Model model, Principal loggedUser) {
        model.addAttribute("principal", loggedUser);
        Profile.Profile_View currentUser = Profile.Profile_View.getView(loggedUser.getName());
        List<Chat.Latest_Chat> latestChats = Chat.Latest_Chat.getWithUser(currentUser.ID);
        model.addAttribute("latestChats", latestChats);
        model.addAttribute("Me", currentUser);
        return "chat";
    }

    @GetMapping("/matches")
    public String showMatchPage(@RequestParam(required = false) Long searchId,
                                @RequestParam(required = false) Integer page,
                                Model model, Principal loggedUser) throws SQLException {
        model.addAttribute("principal", loggedUser);
        User user = User.getByUsername(loggedUser.getName());
        if (searchId == null) searchId = 0L;
        if (page == null) page = 1;
        List<Search_Profile> searchProfiles = Search_Profile.getByUser(user);
        List<Profile.Profile_View> matches = Profile.Profile_View.search(searchId, page);
        while (matches.size() < 10) matches.add(new Profile.Profile_View());
        model.addAttribute("SearchID", searchId);
        model.addAttribute("searchProfiles", searchProfiles);
        model.addAttribute("matches", matches);
        return "matches";
    }

}