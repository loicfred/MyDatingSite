package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class User implements DBSaver<User> {
    public final transient DatabaseEditor<User> DBE = new DatabaseEditor<>(DBM,this, User.class);
    public transient Profile P;

    public long ID;
    public String Username;
    public String Email;
    public String Password;
    public boolean Enabled = false;

    public User() {
        this.ID = Instant.now().toEpochMilli();
    }
    public User(String username, String email, String password) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.Username = username;
        this.Email = email;
        this.Password = password;
        Write();
    }

    public static User getById(long id) {
        return DBM.retrieveItems(User.class).where("ID = ?", id).mapFirstTo(User.class);
    }

    public static User getByUsername(String username) {
        return DBM.retrieveItems(User.class).where("Username = ?", username).mapFirstTo(User.class);
    }

    public static User getByEmail(String email) {
        return DBM.retrieveItems(User.class).where("Email = ?", email).mapFirstTo(User.class);
    }

    public static User getByLogin(String username, String password) {
        return DBM.retrieveItems(User.class).where("(Username = ? OR Email = ?) AND Password = ?", username, username, password).mapFirstTo(User.class);
    }

    public static void ClearFailedLogins(String username, String email) throws SQLException {
        User U = DBM.retrieveItems(User.class).where("Username = ? AND Enabled = ?", username, false).mapFirstTo(User.class);
        if (U != null) U.Delete();
        U = DBM.retrieveItems(User.class).where("Email = ? AND Enabled = ?", email, false).mapFirstTo(User.class);
        if (U != null) U.Delete();
    }

    public static int ClearAllFailedLogins() {
        return DBM.deleteItems(User.class).where("Enabled = ?", false).delete();
    }

    public long getId() {
        return ID;
    }
    public String getUsername() {
        return Username;
    }
    public String getEmail() {
        return Email;
    }
    public String getPassword() {
        return Password;
    }
    public boolean isEnabled() {
        return Enabled;
    }

    public Profile getProfile() throws SQLException {
        return P == null ? P = Profile.getById(ID) : P;
    }

    public void setUsername(String username) {
        Username = DBE.AddSet("Username", username);
    }
    public void setPassword(String password) {
        Password = DBE.AddSet("Password", password);
    }
    public void setEmail(String email) {
        Email = DBE.AddSet("Email", email);
    }
    public void setEnabled(boolean enabled) {
        Enabled = DBE.AddSet("Enabled", enabled);
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
    public User Write() throws SQLException {
         return DBE.Write(false, false);
    }
}