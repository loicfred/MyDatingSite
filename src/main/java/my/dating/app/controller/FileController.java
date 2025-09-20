package my.dating.app.controller;

import my.dating.app.object.Profile;
import my.dating.app.object.Profile_Photo;
import my.dating.app.object.msg.attachment.BaseAttachment;
import my.dating.app.object.msg.attachment.Draft_Attachment;
import my.dating.app.object.msg.attachment.Message_Attachment;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/file")
public class FileController {

    // Profile picture
    @GetMapping("/avatar/{username}.png")
    @Cacheable(value = "profilePic", key = "#username")
    public ResponseEntity<byte[]> getProfilePic(@PathVariable String username) {
        Profile.Profile_View user = Profile.Profile_View.getView(username);
        HttpHeaders headers = new HttpHeaders();
        if (user.Avatar == null) {
            headers.setLocation(URI.create("/img/default-pfp.png"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
            return new ResponseEntity<>(user.Avatar, headers, HttpStatus.FOUND);
        }
    }

    // Banner image
    @GetMapping("/photo/{id}.png")
    @Cacheable(value = "photos", key = "#id")
    public ResponseEntity<byte[]> getBanner(@PathVariable Long id) {
        Profile_Photo photo = Profile_Photo.getById(id);
        if (photo.Image == null) return ResponseEntity.notFound().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(photo.Image, headers, HttpStatus.OK);
    }

    @GetMapping("/message/attachment/{id}")
    @Cacheable(value = "attachments", key = "#id")
    public ResponseEntity<byte[]> getMessageAttachment(@PathVariable Long id) {
        Message_Attachment att = Message_Attachment.getById(id);
        return sendAttachment(att);
    }
    @GetMapping("/draft/attachment/{id}")
    @Cacheable(value = "attachments", key = "#id")
    public ResponseEntity<byte[]> getDraftAttachment(@PathVariable Long id) {
        Draft_Attachment att = Draft_Attachment.getById(id);
        return sendAttachment(att);
    }

    private ResponseEntity<byte[]> sendAttachment(BaseAttachment att) {
        if (att == null) return ResponseEntity.notFound().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(att.getFileType()));
        if (!att.getFileType().contains("image")) headers.setContentDispositionFormData("inline", att.getFileName());
        return new ResponseEntity<>(att.getFileData(), headers, HttpStatus.OK);
    }
}