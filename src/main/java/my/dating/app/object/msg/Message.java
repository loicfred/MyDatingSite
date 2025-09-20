package my.dating.app.object.msg;

import my.dating.app.object.msg.attachment.Message_Attachment;
import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Message extends BaseMessage implements DBSaver<Message> {
    private final transient DatabaseEditor<Message> DBE = new DatabaseEditor<>(DBM,this, Message.class);
    public transient List<Message_Attachment> attachments;
    public transient List<Message_Reaction.Message_Reaction_View> reactions;

    public Long UpdatedAtTime;
    public boolean isEdited = false;
    public boolean isRead = false;

    public Message() {}
    public Message(long chatid, long userid, String content) throws SQLException {
        super(chatid, userid, content);
        this.ID = Instant.now().toEpochMilli();
        Write();
    }

    public static Message getById(long id) {
        return DBM.retrieveItems(Message.class).where("ID = ?", id).mapFirstTo(Message.class);
    }

    public boolean isEdited() {
        return isEdited;
    }
    public boolean isRead() {
        return isRead;
    }

    public List<Message_Attachment> getAttachments() {
        return attachments == null ? attachments = Message_Attachment.getByMessage(ID) : attachments;
    }
    public void addAttachments(Message_Attachment... attachment) {
        if (attachments == null) attachments = new ArrayList<>();
        attachments.addAll(List.of(attachment));
    }
    public int ClearAttachments() {
        return DBM.deleteItems(Message_Attachment.class).where("MessageID = ?", ID).delete();
    }

    public List<Message_Reaction.Message_Reaction_View> getReactions() {
        return reactions == null ? reactions = Message_Reaction.Message_Reaction_View.getByMessage(ID) : reactions;
    }

    public void setUserID(long userid) {
        UserID = DBE.AddSet("UserID", userid);
    }
    public void setContent(String content) {
        if (!content.isEmpty()) Content = DBE.AddSet("Content", content);
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
    public int Update() throws SQLException {
        return DBE.Update("ID = ?", ID);
    }

    @Override
    public int Delete() throws SQLException {
        return DBE.Delete("ID = ?", ID);
    }

    @Override
    public Message Write() throws SQLException {
         return DBE.Write(false, true);
    }

    public static class Message_View extends Message {
        public Long AttachmentCount;
        public Long ReactionCount;

        public Message_View() {}

        public static List<Message_View> getByChat(long chatid, String... select) {
            return DBM.retrieveItems(Message_View.class).select(select).where("ChatID = ?", chatid).mapAllTo(Message_View.class);
        }
        public static Message_View getById(long id) {
            return DBM.retrieveItems(Message_View.class).where("ID = ?", id).mapFirstTo(Message_View.class);
        }

        public Long getAttachmentCount() {
            return AttachmentCount;
        }
        public Long getReactionCount() {
            return ReactionCount;
        }
    }
}
