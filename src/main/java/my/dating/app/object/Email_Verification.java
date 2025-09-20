package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Email_Verification implements DBSaver<Email_Verification> {
    public final transient DatabaseEditor<Email_Verification> DBE = new DatabaseEditor<>(DBM,this, Email_Verification.class);
    private transient User U;

    public long ID;
    public long UserID;
    public String Token;
    public String Type;
    public Long ExpiryDate;

    public Email_Verification() {}
    public Email_Verification(User user, String token, String type) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.UserID = user.ID;
        this.Token = token;
        this.Type = type;
        this.ExpiryDate = Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli();
        Write();
    }

    public static Email_Verification getById(long id) {
        return DBM.retrieveItems(Email_Verification.class).where("ID = ?", id).mapFirstTo(Email_Verification.class);
    }
    public static Email_Verification getByToken(String token) {
        return DBM.retrieveItems(Email_Verification.class).where("Token = ?", token).mapFirstTo(Email_Verification.class);
    }

    public User getUser() {
        return U == null ? U = User.getById(UserID) : U;
    }

    public static void ClearUnregisterUsers() {
        for (Email_Verification vToken : DBM.retrieveItems(Email_Verification.class).where("Type = ? AND ExpiryDate < ?", "REGISTRATION", Instant.now().toEpochMilli()).mapAllTo(Email_Verification.class)) {
            DBM.deleteItems(User.class).where("ID = ?", vToken.UserID).delete();
        }
    }

    @Override
    public int Update() throws SQLException {
        return DBE.Update("ID = ?", ID);
    }

    @Override
    public int Delete() throws SQLException {
        return DBE.Delete("ID = ?", ID);
    }

    @Override
    public Email_Verification Write() throws SQLException {
         return DBE.Write(false, true);
    }
}
