package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Chat implements DBSaver<Chat> {
    public final transient DatabaseEditor<Chat> DBE = new DatabaseEditor<>(DBM,this, Chat.class);
    private transient List<ChatMessage> Messages;
    private transient User U1;
    private transient User U2;

    public long ID;
    public long UserID1;
    public long UserID2;

    public Chat() {}
    public Chat(User user, User user2) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.UserID1 = user.ID;
        this.UserID2 = user2.ID;
        SaveElseWrite();
    }

    public static Chat getById(long id) {
        return DBM.retrieveItems(Chat.class).where("ID = ?", id).mapFirstTo(Chat.class);
    }
    public static Chat getWithUser(User me, User other) throws SQLException {
        Chat C = DBM.retrieveItems(Chat.class).where("(UserID1 = ? OR UserID2 = ?) AND (UserID1 = ? OR UserID2 = ?)", me.ID, me.ID, other.getId(), other.getId()).mapFirstTo(Chat.class);
        return C != null ? C : new Chat(me, other);
    }
    public User getOppositeUser(User me) {
        if (me.ID == UserID1) return getUser2();
        else return getUser1();
    }
    public User getUser1() {
        return U2 == null ? U2 = User.getById(UserID2) : U2;
    }
    public User getUser2() {
        return U2 == null ? U2 = User.getById(UserID2) : U2;
    }
    public List<ChatMessage> getMessages() {
        return Messages == null ? Messages = ChatMessage.getByChat(ID) : Messages;
    }

    @Override
    public Chat Save() throws SQLException {
        DBE.Save("ID = ?", ID); return this;
    }

    @Override
    public Chat SaveElseWrite() throws SQLException {
        DBE.SaveElseWrite("ID = ?", ID); return this;
    }

    @Override
    public Chat Delete() throws SQLException {
        DBE.Delete("ID = ?", ID); return this;
    }
}
