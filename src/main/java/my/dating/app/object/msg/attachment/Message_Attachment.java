package my.dating.app.object.msg.attachment;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Message_Attachment extends BaseAttachment implements DBSaver<Message_Attachment> {
    private final transient DatabaseEditor<Message_Attachment> DBE = new DatabaseEditor<>(DBM,this, Message_Attachment.class);

    public Message_Attachment() {}
    public Message_Attachment(Draft_Attachment draftAttachment, long messageID) throws SQLException {
        super(messageID, draftAttachment.FileName, draftAttachment.FileType, draftAttachment.FileData);
        Write();
    }
    public Message_Attachment(long messageID, String fileName, String fileType, byte[] fileData) throws SQLException {
        super(messageID, fileName, fileType, fileData);
        Write();
    }

    public static Message_Attachment getById(long id, String... select) {
        return DBM.retrieveItems(Message_Attachment.class).select(select).where("ID = ?", id).mapFirstTo(Message_Attachment.class);
    }
    public static List<Message_Attachment> getByMessage(long messageID, String... select) {
        return DBM.retrieveItems(Message_Attachment.class).select(select).where("MessageID = ?", messageID).mapAllTo(Message_Attachment.class);
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
    public Message_Attachment Write() throws SQLException {
         return DBE.Write(false, true);
    }

}
