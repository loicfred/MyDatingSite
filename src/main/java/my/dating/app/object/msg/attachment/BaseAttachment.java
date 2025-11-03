package my.dating.app.object.msg.attachment;

import my.dating.app.service.DatabaseObject;

import java.time.Instant;

public class BaseAttachment<T> extends DatabaseObject<T> {
    public long ID = Instant.now().toEpochMilli();
    public long MessageID;
    public String FileName;
    public String FileType;
    public byte[] FileData;

    public BaseAttachment() {}
    public BaseAttachment(long messageID, String fileName, String fileType, byte[] fileData) {
        this.MessageID = messageID;
        this.FileName = fileName;
        this.FileType = fileType;
        this.FileData = fileData;
    }

    public long getID() {
        return ID;
    }
    public long getMessageID() {
        return MessageID;
    }
    public String getFileName() {
        return FileName;
    }
    public String getFileType() {
        return FileType;
    }
    public byte[] getFileData() {
        return FileData;
    }

}
