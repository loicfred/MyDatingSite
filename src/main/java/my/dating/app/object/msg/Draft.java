package my.dating.app.object.msg;

import my.dating.app.object.msg.attachment.Draft_Attachment;
import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Draft extends BaseMessage implements DBSaver<Draft> {
    private final transient DatabaseEditor<Draft> DBE = new DatabaseEditor<>(DBM,this, Draft.class);
    private transient List<Draft_Attachment> attachments;

    public Draft() {}
    public Draft(long chatid, long userid, String content) {
        super(chatid, userid, content);
    }

    public static Draft getById(long id) {
        return DBM.retrieveItems(Draft.class).where("ID = ?", id).mapFirstTo(Draft.class);
    }
    public static Draft get(long chatid, long userid) {
        return DBM.retrieveItems(Draft.class).where("ChatID = ? AND UserID = ?", chatid, userid).mapFirstTo(Draft.class);
    }
    public List<Draft_Attachment> getAttachments() {
        return attachments == null ? attachments = Draft_Attachment.getByDraft(ID) : attachments;
    }
    public void addAttachments(Draft_Attachment... attachment) {
        if (attachments == null) attachments = new ArrayList<>();
        attachments.addAll(List.of(attachment));
    }
    public int ClearAttachments() {
        return DBM.deleteItems(Draft_Attachment.class).where("MessageID = ?", ID).delete();
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
    public Draft Write() throws SQLException {
        return DBE.Write(true, true);
    }
}
