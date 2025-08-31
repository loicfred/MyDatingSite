package my.dating.app.object.enums;

public enum KidsPreference {
    ONE("Only one"),
    MULTIPLE("Siblings"),
    WANT_BUT_CANT("Yes, but can’t (via adoption/other means)"),
    NONE("No");

    private final String displayName;

    KidsPreference(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
