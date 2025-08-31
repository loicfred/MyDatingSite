package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Profile_Photo implements DBSaver<Profile_Photo> {
    public final transient DatabaseEditor<Profile_Photo> DBE = new DatabaseEditor<>(DBM,this, Profile_Photo.class);

    public long ID;
    public long UserID;
    public byte[] Image;

    public Profile_Photo() {}
    public Profile_Photo(User user, byte[] img) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.UserID = user.ID;
        this.Image = img;
        SaveElseWrite();
    }

    public static Profile_Photo getById(long id) {
        return DBM.retrieveItems(Profile_Photo.class).where("ID = ?", id).mapFirstTo(Profile_Photo.class);
    }
    public static List<Profile_Photo> getByUser(long userid) {
        return DBM.retrieveItems(Profile_Photo.class).where("UserID = ?", userid).mapAllTo(Profile_Photo.class);
    }

    @Override
    public Profile_Photo Save() throws SQLException {
        DBE.Save("ID = ?", ID); return this;
    }

    @Override
    public Profile_Photo SaveElseWrite() throws SQLException {
        DBE.SaveElseWrite("ID = ?", ID); return this;
    }

    @Override
    public Profile_Photo Delete() throws SQLException {
        DBE.Delete("ID = ?", ID); return this;
    }
}
