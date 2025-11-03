package my.dating.app.object;

import my.dating.app.service.DatabaseObject;

import java.time.Instant;
import java.util.List;

public class Profile_Photo extends DatabaseObject<Profile_Photo> {

    public long ID;
    public long UserID;
    public byte[] Image;

    public Profile_Photo() {}
    public Profile_Photo(User user, byte[] img) {
        this.ID = Instant.now().toEpochMilli();
        this.UserID = user.ID;
        this.Image = img;
        Write();
    }

    public static Profile_Photo getById(long id) {
        return DatabaseObject.getById(Profile_Photo.class, id).orElse(null);
    }
    public static List<Profile_Photo> getByUser(long userid) {
        return DatabaseObject.getAllWhere(Profile_Photo.class, "UserID = ?", userid);
    }
}
