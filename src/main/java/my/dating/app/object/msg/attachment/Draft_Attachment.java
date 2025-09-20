package my.dating.app.object.msg.attachment;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Draft_Attachment extends BaseAttachment implements DBSaver<Draft_Attachment> {
    private final transient DatabaseEditor<Draft_Attachment> DBE = new DatabaseEditor<>(DBM,this, Draft_Attachment.class);

    public Draft_Attachment() {}
    public Draft_Attachment(long messageID, String fileName, String fileType, byte[] fileData) throws SQLException {
        super(messageID, fileName, fileType, fileData);
        Write();
    }

    public static Draft_Attachment getById(long id) {
        return DBM.retrieveItems(Draft_Attachment.class).where("ID = ?", id).mapFirstTo(Draft_Attachment.class);
    }
    public static List<Draft_Attachment> getByDraft(long draftMessageID, String... select) {
        return DBM.retrieveItems(Draft_Attachment.class).select(select).where("MessageID = ?", draftMessageID).mapAllTo(Draft_Attachment.class);
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
    public Draft_Attachment Write() throws SQLException {
        return DBE.Write(false, true);
    }

}
