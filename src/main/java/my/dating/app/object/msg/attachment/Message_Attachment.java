package my.dating.app.object.msg.attachment;

import my.dating.app.service.DatabaseObject;

import java.util.List;

public class Message_Attachment extends BaseAttachment<Message_Attachment> {

    public Message_Attachment() {}
    public Message_Attachment(Draft_Attachment draftAttachment, long messageID) {
        super(messageID, draftAttachment.FileName, draftAttachment.FileType, draftAttachment.FileData);
        Write();
    }
    public Message_Attachment(long messageID, String fileName, String fileType, byte[] fileData) {
        super(messageID, fileName, fileType, fileData);
        Write();
    }

    public static Message_Attachment getById(long id) {
        return DatabaseObject.getById(Message_Attachment.class, id).orElse(null);
    }
    public static List<Message_Attachment> getByMessage(long messageID) {
        return DatabaseObject.getAllWhere(Message_Attachment.class, "MessageID = ?", messageID);
    }

}
