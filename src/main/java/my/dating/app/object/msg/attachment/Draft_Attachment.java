package my.dating.app.object.msg.attachment;

import my.dating.app.service.DatabaseObject;

import java.util.List;

public class Draft_Attachment extends BaseAttachment<Draft_Attachment> {

    public Draft_Attachment() {}
    public Draft_Attachment(long messageID, String fileName, String fileType, byte[] fileData) {
        super(messageID, fileName, fileType, fileData);
        Write();
    }

    public static Draft_Attachment getById(long id) {
        return DatabaseObject.getById(Draft_Attachment.class, id).orElse(null);
    }
    public static List<Draft_Attachment> getByDraft(long draftMessageID) {
        return DatabaseObject.getAllWhere(Draft_Attachment.class, "MessageID = ?", draftMessageID);
    }

}
