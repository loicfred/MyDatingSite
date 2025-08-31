package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class ChatMessage implements DBSaver<ChatMessage> {
    public final transient DatabaseEditor<ChatMessage> DBE = new DatabaseEditor<>(DBM,this, ChatMessage.class);
    private transient User U;

    public long ID;
    public long ChatID;
    public long UserID;
    public String Message;

    public ChatMessage() {}
    public ChatMessage(long chatid, User user, String message) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.ChatID = chatid;
        this.UserID = user.ID;
        this.Message = message;
        SaveElseWrite();
    }

    public User getUser() {
        return U == null ? U = User.getById(UserID) : U;
    }

    public static ChatMessage getById(long id) {
        return DBM.retrieveItems(ChatMessage.class).where("ID = ?", id).mapFirstTo(ChatMessage.class);
    }
    public static List<ChatMessage> getByChat(long chatid) {
        return DBM.retrieveItems(ChatMessage.class).where("ChatID = ?", chatid).mapAllTo(ChatMessage.class);
    }
    public static ChatMessage getByUser(long userid) {
        return DBM.retrieveItems(ChatMessage.class).where("UserID = ?", userid).mapFirstTo(ChatMessage.class);
    }

    public long getID() {
        return ID;
    }
    public long getChatID() {
        return ChatID;
    }
    public long getUserID() {
        return UserID;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        if (!message.isEmpty()) Message = DBE.AddSet("Message", message);
    }

    @Override
    public ChatMessage Save() throws SQLException {
        DBE.Save("ID = ?", ID); return this;
    }

    @Override
    public ChatMessage SaveElseWrite() throws SQLException {
        DBE.SaveElseWrite("ID = ?", ID); return this;
    }

    @Override
    public ChatMessage Delete() throws SQLException {
        DBE.Delete("ID = ?", ID); return this;
    }
}
