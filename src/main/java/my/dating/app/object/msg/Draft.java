package my.dating.app.object.msg;

import my.dating.app.object.msg.attachment.Draft_Attachment;
import my.dating.app.service.DatabaseObject;

import java.util.ArrayList;
import java.util.List;

public class Draft extends BaseMessage<Draft> {
    private transient List<Draft_Attachment> attachments;

    public Draft() {}
    public Draft(long chatid, long userid, String content) {
        super(chatid, userid, content);
    }

    public static Draft getById(long id) {
        return DatabaseObject.getById(Draft.class, id).orElse(null);
    }
    public static Draft get(long chatid, long userid) {
        return DatabaseObject.getWhere(Draft.class, "ChatID = ? AND UserID = ?", chatid, userid).orElse(null);
    }

    public List<Draft_Attachment> getAttachments() {
        return attachments == null ? attachments = Draft_Attachment.getByDraft(ID) : attachments;
    }
    public void addAttachments(Draft_Attachment... attachment) {
        if (attachments == null) attachments = new ArrayList<>();
        attachments.addAll(List.of(attachment));
    }
    public int ClearAttachments() {
        return DatabaseObject.doUpdate("DELETE FROM draft_attachment WHERE MessageID = ?", ID);
    }

}
