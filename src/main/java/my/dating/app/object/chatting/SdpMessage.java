package my.dating.app.object.chatting;

// SdpMessage.java
public class SdpMessage {
    private String from;
    private String to;
    private String sdp;

    public SdpMessage() {}

    public SdpMessage(String from, String to, String sdp) {
        this.from = from;
        this.to = to;
        this.sdp = sdp;
    }

    // Getters & setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getSdp() { return sdp; }
    public void setSdp(String sdp) { this.sdp = sdp; }
}