package my.dating.app.controller;

import my.dating.app.object.Profile;
import my.dating.app.object.Profile_Photo;
import my.dating.app.object.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

@Controller
public class ProfileController {

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable String username, Model model, Principal loggedUser) throws SQLException {
        model.addAttribute("principal", loggedUser);
        User user = User.getByUsername(username);
        while (user.getProfile().getPhotos().size() < 20) user.getProfile().getPhotos().add(new Profile_Photo());
        model.addAttribute("user", user);
        model.addAttribute("isOwner", loggedUser != null && loggedUser.getName().equals(user.getUsername()));
        return "profile/view";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Principal loggedUser) throws SQLException {
        model.addAttribute("principal", loggedUser);
        String username = loggedUser.getName();
        User user = User.getByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("profile", user.getProfile());
        return "profile/edit";
    }

    @PostMapping("/post/profile/edit")
    public String saveProfile(@ModelAttribute Profile profile, Principal loggedUser, @RequestParam("profilePicFile") MultipartFile profilePicFile) throws IOException, SQLException {
        String username = loggedUser.getName();
        if (profile.ID == User.getByUsername(username).ID) {
            if (!profilePicFile.isEmpty()) profile.setAvatar(profilePicFile.getBytes());
            profile.SaveElseWrite();
        }
        return "redirect:/profile/edit?updated";
    }

}
