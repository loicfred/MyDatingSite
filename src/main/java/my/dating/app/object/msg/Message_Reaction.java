package my.dating.app.object.msg;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Message_Reaction implements DBSaver<Message_Reaction> {
    private final transient DatabaseEditor<Message_Reaction> DBE = new DatabaseEditor<>(DBM,this, Message_Reaction.class);

    public long ID = Instant.now().toEpochMilli();
    public long MessageID;
    public long UserID;
    public String Emoji;

    public Message_Reaction() {}
    public Message_Reaction(long userID, long messageID, String emoji) throws SQLException {
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
        return DBM.retrieveItems(Message_Reaction.class).where("ID = ?", id).mapFirstTo(Message_Reaction.class);
    }
    public static Message_Reaction get(long userid, long messageid, String emoji) {
        return DBM.retrieveItems(Message_Reaction.class).where("UserID = ? AND MessageID = ? AND Emoji = ?", userid, messageid, emoji).mapFirstTo(Message_Reaction.class);
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
    public Message_Reaction Write() throws SQLException {
        return DBE.Write(true, true);
    }

    public static class Message_Reaction_View extends Message_Reaction {
        public long Count = 0;

        public Message_Reaction_View() {}

        public static List<Message_Reaction_View> getByMessage(long messageID) {
            return DBM.retrieveItems(Message_Reaction_View.class).where("MessageID = ?", messageID).mapAllTo(Message_Reaction_View.class);
        }

        public long getCount() {
            return Count;
        }
    }
}
