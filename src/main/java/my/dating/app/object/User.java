package my.dating.app.object;

import my.dating.app.service.DatabaseObject;

import java.time.Instant;

public class User extends DatabaseObject<User> {
    public transient Profile P;

    public long ID;
    public String Username;
    public String Email;
    public String Password;
    public boolean Enabled = false;

    public User() {
        this.ID = Instant.now().toEpochMilli();
    }
    public User(String username, String email, String password) {
        this.ID = Instant.now().toEpochMilli();
        this.Username = username;
        this.Email = email;
        this.Password = password;
        Write();
    }

    public static User getById(long id) {
        return DatabaseObject.getById(User.class, id).orElse(null);
    }

    public static User getByUsername(String username) {
        return DatabaseObject.getWhere(User.class, "Username = ?", username).orElse(null);
    }

    public static User getByEmail(String email) {
        return DatabaseObject.getWhere(User.class, "Email = ?", email).orElse(null);
    }

    public static User getByLogin(String username, String password) {
        return DatabaseObject.getWhere(User.class, "(Username = ? OR Email = ?) AND Password = ?", username, username, password).orElse(null);
    }

    public static void ClearFailedLogins(String username, String email) {
        User U = DatabaseObject.getWhere(User.class, "Username = ? OR Enabled = ?", username, false).orElse(null);
        if (U != null) U.Delete();
        U = DatabaseObject.getWhere(User.class, "Email = ? OR Enabled = ?", email, false).orElse(null);
        if (U != null) U.Delete();
    }

    public static int ClearAllFailedLogins() {
        return DatabaseObject.doUpdate("DELETE user WHERE Enabled = ?", false);
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

    public void setUsername(String username) {
        Username = username;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }



    public Profile getProfile() {
        return P == null ? P = Profile.getById(ID) : P;
    }
}