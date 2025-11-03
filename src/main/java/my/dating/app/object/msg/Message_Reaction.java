package my.dating.app.object.msg;

import my.dating.app.service.DatabaseObject;

import java.time.Instant;
import java.util.List;

public class Message_Reaction extends DatabaseObject<Message_Reaction> {

    public long ID = Instant.now().toEpochMilli();
    public long MessageID;
    public long UserID;
    public String Emoji;

    public Message_Reaction() {}
    public Message_Reaction(long userID, long messageID, String emoji) {
        MessageID = messageID;
        UserID = userID;
        Emoji = emoji;
        Write();
    }

    public long getID() {
        return ID;
    }
    public long getMessageID() {
        return MessageID;
    }
    public long getUserID() {
        return UserID;
    }
    public String getEmoji() {
        return Emoji;
    }

    public static Message_Reaction getById(long id) {
        return DatabaseObject.getById(Message_Reaction.class, id).orElse(null);
    }
    public static Message_Reaction get(long userid, long messageid, String emoji) {
        return DatabaseObject.getWhere(Message_Reaction.class, "UserID = ? AND MessageID = ? AND Emoji = ?", userid, messageid, emoji).orElse(null);
    }

    public static class Message_Reaction_View extends Message_Reaction {
        public long Count = 0;

        public Message_Reaction_View() {}

        public static List<Message_Reaction_View> getByMessage(long messageID) {
            return DatabaseObject.getAllWhere(Message_Reaction_View.class, "MessageID = ?", messageID);
        }

        public long getCount() {
            return Count;
        }
    }
}
