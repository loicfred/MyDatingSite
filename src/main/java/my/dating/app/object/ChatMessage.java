package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class ChatMessage implements DBSaver<ChatMessage> {
    public final transient DatabaseEditor<ChatMessage> DBE = new DatabaseEditor<>(DBM,this, ChatMessage.class);

    public long ID = Instant.now().toEpochMilli();
    public long ChatID;
    public long UserID;
    public String Message;
    public Long UpdatedAtTime;
    public boolean isEdited = false;
    public boolean isRead = false;

    public ChatMessage() {}
    public ChatMessage(long chatid, User user, String message) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.ChatID = chatid;
        this.UserID = user.ID;
        this.Message = message;
        SaveElseWrite();
    }

    public static ChatMessage getById(long id) {
        return DBM.retrieveItems(ChatMessage.class).where("ID = ?", id).mapFirstTo(ChatMessage.class);
    }
    public static List<ChatMessage> getByChat(long chatid, String... select) {
        return DBM.retrieveItems(ChatMessage.class).select(select).where("ChatID = ?", chatid).mapAllTo(ChatMessage.class);
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
    public boolean isEdited() {
        return isEdited;
    }
    public boolean isRead() {
        return isRead;
    }

    public void setUserID(long userid) {
        UserID = DBE.AddSet("UserID", userid);
    }
    public void setMessage(String message) {
        if (!message.isEmpty()) Message = DBE.AddSet("Message", message);
    }
    public void setEdited(boolean edited) {
        isEdited = DBE.AddSet("isEdited", edited);
    }
    public void setRead(boolean edited) {
        isRead = DBE.AddSet("isRead", edited);
    }
    public void setUpdatedAtTime(Long time) {
        UpdatedAtTime = DBE.AddSet("UpdatedAtTime", time);
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
