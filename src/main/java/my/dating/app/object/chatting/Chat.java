package my.dating.app.object.chatting;

import my.dating.app.object.User;
import my.dating.app.object.msg.Message;
import my.dating.app.service.DatabaseObject;

import java.time.Instant;
import java.util.List;

public class Chat extends DatabaseObject<Chat> {
    private transient List<Message.Message_View> Messages;

    public long ID;
    public long UserID1;
    public long UserID2;

    public Chat() {}
    public Chat(User user, User user2) {
        this.ID = Instant.now().toEpochMilli();
        this.UserID1 = user.ID;
        this.UserID2 = user2.ID;
        Write();
    }

    public static Chat getById(long id) {
        return DatabaseObject.getById(Chat.class, id).orElse(null);
    }
    public List<Message.Message_View> getMessages() {
        return Messages == null ? Messages = Message.Message_View.getByChat(ID) : Messages;
    }

    public void readAllMessages(long userid) {
        DatabaseObject.doUpdate("UPDATE message SET isRead = ? WHERE ChatID = ? AND UserID = ?", true, ID, userid);
    }

    public static class Chatlist_Item extends Chat {

        public Long LatestMessageID;
        public Long SenderID;
        public String Content;

        public Long PartnerID;
        public String PartnerUsername;
        public String PartnerName;

        public Long Unreads;

        public static List<Chatlist_Item> getWithUser(long userid) {
            return DatabaseObject.getAllWhere(Chatlist_Item.class, "(UserID1 = ? OR UserID2 = ?) AND NOT PartnerID = ?", userid, userid, userid);
        }
        public static Chatlist_Item find(long chatid, String username) {
            return DatabaseObject.getWhere(Chatlist_Item.class, "ID = ? AND NOT PartnerUsername = ?", chatid, username).orElse(null);
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
