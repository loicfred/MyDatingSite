package my.dating.app.object.msg;

import my.dating.app.service.DatabaseObject;

public class BaseMessage<T> extends DatabaseObject<T> {
    public Long ID;
    public long ChatID;
    public long UserID;
    public String Content;

    public BaseMessage() {}
    public BaseMessage(long chatid, long userid, String content) {
        this.ChatID = chatid;
        this.UserID = userid;
        this.Content = content;
    }

    public Long getID() {
        return ID;
    }
    public long getChatID() {
        return ChatID;
    }
    public long getUserID() {
        return UserID;
    }
    public String getContent() {
        return Content;
    }
}
