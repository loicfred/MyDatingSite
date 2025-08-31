package my.dating.app.object.enums;

public enum PersonalityType {
    EXTROVERT("Extrovert"),
    ENERGETIC("Energetic"),
    CHILL("Chill"),
    CUTE("Cute"),
    SHY("Shy"),
    INTROVERT("Introvert");

    private final String displayName;

    PersonalityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
