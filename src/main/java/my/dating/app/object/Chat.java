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
    public List<ChatMessage> getMessages(String... select) {
        return Messages == null ? Messages = ChatMessage.getByChat(ID, select) : Messages;
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

    public static class Latest_Chat extends Chat {

        public Long LatestMessageID;
        public Long SenderID;
        public String Content;

        public Long UserID;
        public String Username;
        public String Name;

        public static List<Latest_Chat> getWithUser(long userid) {
            return DBM.retrieveItems(Latest_Chat.class).where("(UserID1 = ? OR UserID2 = ?) AND NOT UserID = ?", userid, userid, userid).mapAllTo(Latest_Chat.class);
        }
        public static Latest_Chat find(long chatid, String username) {
            return DBM.retrieveItems(Latest_Chat.class).where("ID = ? AND NOT Username = ?", chatid, username).mapFirstTo(Latest_Chat.class);
        }

        public Long getLatestMessageID() {
            return LatestMessageID;
        }
        public void setLatestMessageID(Long latestMessageID) {
            LatestMessageID = latestMessageID;
        }
        public Long getSenderID() {
            return SenderID;
        }
        public void setSenderID(Long senderID) {
            SenderID = senderID;
        }
        public void setUserID(Long userID) {
            UserID = userID;
        }
        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }
        public String getUsername() {
            return Username;
        }
        public void setUsername(String username) {
            Username = username;
        }
        public String getName() {
            return Name;
        }
        public Long getUserID() {
            return UserID;
        }
        public void setName(String name) {
            Name = name;
        }
    }
}
