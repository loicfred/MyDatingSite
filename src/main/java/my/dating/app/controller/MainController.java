package my.dating.app.controller;

import my.dating.app.object.Profile;
import my.dating.app.object.Profile_Photo;
import my.dating.app.object.Search_Profile;
import my.dating.app.object.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String defaultPage(Model model) {
        model.addAttribute("isAnonymous", SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        return "home";
    }
    @GetMapping("/home")
    public String home(Model model) {
        return defaultPage(model);
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable String username, Model model) throws SQLException {
        model.addAttribute("isAnonymous", SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        User user = User.getByUsername(username);
        while (user.getProfile().getPhotos().size() < 20) user.getProfile().getPhotos().add(new Profile_Photo());
        model.addAttribute("user", user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isOwner = auth != null && auth.getName().equals(user.getUsername());
        model.addAttribute("isOwner", isOwner);
        return "profile/view";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) throws SQLException {
        model.addAttribute("isAnonymous", SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = User.getByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("profile", user.getProfile());
        return "profile/edit";
    }

    @PostMapping("/post/profile/edit")
    public String saveProfile(@ModelAttribute Profile profile, @RequestParam("profilePicFile") MultipartFile profilePicFile) throws IOException, SQLException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (profile.ID == User.getByUsername(username).ID) {
            if (!profilePicFile.isEmpty()) profile.setAvatar(profilePicFile.getBytes());
            profile.SaveElseWrite();
        }
        return "redirect:/profile/edit?updated";
    }

    @GetMapping("/matches")
    public String showMatchPage(@RequestParam(required = false) Long searchId,
                                @RequestParam(required = false) Integer page,
                                Model model) throws SQLException {
        model.addAttribute("isAnonymous", SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = User.getByUsername(auth.getName());
        if (searchId == null) searchId = 0L;
        if (page == null) page = 1;
        List<Search_Profile> searchProfiles = Search_Profile.getByUser(user);
        List<Profile> matches = Profile.search(searchId, page);
        while (matches.size() < 10) matches.add(new Profile());
        model.addAttribute("SearchID", searchId);
        model.addAttribute("searchProfiles", searchProfiles);
        model.addAttribute("matches", matches);
        return "matches";
    }

}