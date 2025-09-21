package my.dating.app.object;

import my.dating.app.object.msg.Message;
import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;
import my.utilities.db.QueryParameter;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Chat implements DBSaver<Chat> {
    public final transient DatabaseEditor<Chat> DBE = new DatabaseEditor<>(DBM,this, Chat.class);
    private transient List<Message.Message_View> Messages;

    public long ID;
    public long UserID1;
    public long UserID2;

    public Chat() {}
    public Chat(User user, User user2) throws SQLException {
        this.ID = Instant.now().toEpochMilli();
        this.UserID1 = user.ID;
        this.UserID2 = user2.ID;
        Write();
    }

    public static Chat getById(long id) {
        return DBM.retrieveItems(Chat.class).where("ID = ?", id).mapFirstTo(Chat.class);
    }
    public List<Message.Message_View> getMessages(String... select) {
        return Messages == null ? Messages = Message.Message_View.getByChat(ID, select) : Messages;
    }

    public void readAllMessages(long userid) {
        DBM.updateItems(Message.class).set(new QueryParameter("isRead", true)).where("ChatID = ? AND UserID = ?", ID, userid).update();
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
    public Chat Write() throws SQLException {
         return DBE.Write(false, true);
    }

    public static class Latest_Chat extends Chat {

        public Long LatestMessageID;
        public Long SenderID;
        public String Content;

        public Long PartnerID;
        public String PartnerUsername;
        public String PartnerName;

        public Long Unreads;

        public static List<Latest_Chat> getWithUser(long userid) {
            return DBM.retrieveItems(Latest_Chat.class).where("(UserID1 = ? OR UserID2 = ?) AND NOT PartnerID = ?", userid, userid, userid).mapAllTo(Latest_Chat.class);
        }
        public static Latest_Chat find(long chatid, String username) {
            return DBM.retrieveItems(Latest_Chat.class).where("ID = ? AND NOT PartnerUsername = ?", chatid, username).mapFirstTo(Latest_Chat.class);
        }

        public Long getLatestMessageID() {
            return LatestMessageID;
        }
        public Long getSenderID() {
            return SenderID;
        }
        public String getContent() {
            return Content;
        }
        public String getPartnerUsername() {
            return PartnerUsername;
        }
        public String getPartnerName() {
            return PartnerName;
        }
        public Long getPartnerID() {
            return PartnerID;
        }
        public Long getUnreads() {
            return Unreads;
        }

        public void setLatestMessageID(Long latestMessageID) {
            LatestMessageID = latestMessageID;
        }
        public void setSenderID(Long senderID) {
            SenderID = senderID;
        }
        public void setContent(String content) {
            Content = content;
        }
        public void setPartnerUsername(String username) {
            PartnerUsername = username;
        }
        public void setPartnerName(String name) {
            PartnerName = name;
        }
        public void setUnreads(Long unreads) {
            Unreads = unreads;
        }

        public long getMyId() {
            return PartnerID == UserID2 ? UserID1 : UserID2;
        }

    }
}
