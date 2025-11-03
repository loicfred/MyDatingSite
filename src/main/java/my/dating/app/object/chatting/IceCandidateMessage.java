package my.dating.app.object.chatting;

public class IceCandidateMessage {
    private String from;
    private String to;
    private String candidate;

    public IceCandidateMessage() {}

    public IceCandidateMessage(String from, String to, String candidate) {
        this.from = from;
        this.to = to;
        this.candidate = candidate;
    }

    // Getters & setters
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getCandidate() { return candidate; }
    public void setCandidate(String candidate) { this.candidate = candidate; }
}