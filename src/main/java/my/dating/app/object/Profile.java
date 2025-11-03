package my.dating.app.object;

import my.dating.app.object.enums.*;
import my.dating.app.service.DatabaseObject;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Profile extends DatabaseObject<Profile> {
    private transient List<Profile_Photo> photos;

    public long ID = 0;
    public long getID() {
        return ID;
    }
    public void setID(long id) {
        ID = id;
    }

    public String Name;
    public String getName() {
        return Name;
    }
    public void setName(String val) {
        Name = val;
    }

    public String Gender;
    public String getGender() {
        return Gender;
    }
    public void setGender(String val) {
        if (!val.isEmpty()) Gender = my.dating.app.object.enums.Gender.valueOf(val).name();
    }

    public byte[] Avatar = null;
    public void setAvatar(byte[] avatar) {
        Avatar = avatar;
    }

    public LocalDate DateOfBirth;
    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }
    public void setDateOfBirth(LocalDate val) {
        if (val != null) DateOfBirth = val;
    }

    public String WhoAmI;
    public String getWhoAmI() {
        return WhoAmI;
    }
    public void setWhoAmI(String val) {
        if (!val.isEmpty()) WhoAmI = val;
    }

    public String WhatIWant;
    public String getWhatIWant() {
        return WhatIWant;
    }
    public void setWhatIWant(String val) {
        if (!val.isEmpty()) WhatIWant = val;
    }

    public String WhatIDislike;
    public String getWhatIDislike() {
        return WhatIDislike;
    }
    public void setWhatIDislike(String val) {
        if (!val.isEmpty()) WhatIDislike = val;
    }

    public String PersonalityType;
    public String getPersonalityType() {
        return PersonalityType;
    }
    public void setPersonalityType(String val) {
        if (!val.isEmpty()) PersonalityType = my.dating.app.object.enums.PersonalityType.valueOf(val).name();
    }

    public transient Set<Interest> interests;
    public Set<Interest> getInterests() {
        return interests == null ? interests = DatabaseObject.doQuery("SELECT Value FROM profile_to_interest WHERE UserID = ?", ID).stream()
                .map(row -> Interest.valueOf(row.get("Value").toString())).collect(Collectors.toSet()) : interests;
    }
    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
        DatabaseObject.doUpdate("DELETE FROM profile_to_interest WHERE UserID = ?", ID);
        for (Interest I : interests)
            DatabaseObject.doUpdate("INSERT INTO profile_to_interest (UserID, Value) VALUES (?,?)", ID, I.name());
    }

    public transient Set<Language> languages;
    public Set<Language> getLanguages() {
        return languages == null ? languages = DatabaseObject.doQuery("SELECT Value FROM profile_to_language WHERE UserID = ?", ID).stream()
                .map(row -> Language.valueOf(row.get("Value").toString())).collect(Collectors.toSet()) : languages;
    }
    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
        DatabaseObject.doUpdate("DELETE FROM profile_to_language WHERE UserID = ?", ID);
        for (Language I : languages)
            DatabaseObject.doUpdate("INSERT INTO profile_to_language (UserID, Value) VALUES (?,?)", ID, I.name());
    }

    public Double Latitude;
    public Double Longitude;
    public Double getLatitude() {
        return Latitude;
    }
    public Double getLongitude() {
        return Longitude;
    }
    public void setLatitude(Double val) {
        Latitude = val;
    }
    public void setLongitude(Double val) {
        Longitude = val;
    }


    public String LifestyleEducation;
    public String getLifestyleEducation() {
        return LifestyleEducation;
    }
    public void setLifestyleEducation(String val) {
        if (!val.isEmpty()) LifestyleEducation = val;
    }

    public String LifestyleOccupation;
    public String getLifestyleOccupation() {
        return LifestyleOccupation;
    }
    public void setLifestyleOccupation(String val) {
        if (!val.isEmpty()) LifestyleOccupation = val;
    }

    public String LifestyleReligion;
    public String getLifestyleReligion() {
        return LifestyleReligion;
    }
    public void setLifestyleReligion(String val) {
        if (!val.isEmpty()) LifestyleReligion = my.dating.app.object.enums.Religion.valueOf(val).name();
    }

    public String LifestyleDiet;
    public String getLifestyleDiet() {
        return LifestyleDiet;
    }
    public void setLifestyleDiet(String val) {
        if (!val.isEmpty()) LifestyleDiet = Diet.valueOf(val).name();
    }

    public String LifestyleWouldCook;
    public String getLifestyleWouldCook() {
        return LifestyleWouldCook;
    }
    public void setLifestyleWouldCook(String val) {
        if (!val.isEmpty()) LifestyleWouldCook = LifestylePreference.valueOf(val).name();
    }

    public String LifestyleWouldChore;
    public String getLifestyleWouldChore() {
        return LifestyleWouldChore;
    }
    public void setLifestyleWouldChore(String val) {
        if (!val.isEmpty()) LifestyleWouldChore = LifestylePreference.valueOf(val).name();
    }

    public String LifestyleSmoking;
    public String getLifestyleSmoking() {
        return LifestyleSmoking;
    }
    public void setLifestyleSmoking(String val) {
        if (!val.isEmpty()) LifestyleSmoking = BadHabit.valueOf(val).name();
    }

    public String LifestyleDrinking;
    public String getLifestyleDrinking() {
        return LifestyleDrinking;
    }
    public void setLifestyleDrinking(String val) {
        if (!val.isEmpty()) LifestyleDrinking = BadHabit.valueOf(val).name();
    }

    public String LifestyleExercise;
    public String getLifestyleExercise() {
        return LifestyleExercise;
    }
    public void setLifestyleExercise(String val) {
        if (!val.isEmpty()) LifestyleExercise = BadHabit.valueOf(val).name();
    }

    public String LifestyleAllergies;
    public String getLifestyleAllergies() {
        return LifestyleAllergies;
    }
    public void setLifestyleAllergies(String val) {
        if (!val.isEmpty()) LifestyleAllergies = val;
    }

    public String LifestyleDisability;
    public String getLifestyleDisability() {
        return LifestyleDisability;
    }
    public void setLifestyleDisability(String val) {
        if (!val.isEmpty()) LifestyleDisability = val;
    }

    public Boolean LifestylePets;
    public Boolean getLifestylePets() {
        return LifestylePets;
    }
    public void setLifestylePets(boolean val) {
        LifestylePets = val;
    }


    public boolean MessageLeaveOnRead = false;
    public boolean getMessageLeaveOnRead() {
        return MessageLeaveOnRead;
    }
    public void setMessageLeaveOnRead(boolean val) {
        MessageLeaveOnRead = val;
    }

    public String MessageReplySpeed;
    public String getMessageReplySpeed() {
        return MessageReplySpeed;
    }
    public void setMessageReplySpeed(String val) {
        if (!val.isEmpty()) MessageReplySpeed = Rapidity.valueOf(val).name();
    }


    public int LovelifePastPartners = 0;
    public int getLovelifePastPartners() {
        return LovelifePastPartners;
    }
    public void setLovelifePastPartners(int val) {
        LovelifePastPartners = Math.max(0,val);
    }

    public boolean LovelifeVirginity = false;
    public boolean getLovelifeVirginity() {
        return LovelifeVirginity;
    }
    public void setLovelifeVirginity(boolean val) {
        LovelifeVirginity = val;
    }

    public boolean LovelifeMarriage = true;
    public boolean getLovelifeMarriage() {
        return LovelifeMarriage;
    }
    public void setLovelifeMarriage(boolean val) {
        LovelifeMarriage = val;
    }

    public boolean LovelifeReligionMatter = false;
    public boolean getLovelifeReligionMatter() {
        return LovelifeReligionMatter;
    }
    public void setLovelifeReligionMatter(boolean val) {
        LovelifeReligionMatter = val;
    }

    public String LovelifeWantKids;
    public String getLovelifeWantKids() {
        return LovelifeWantKids;
    }
    public void setLovelifeWantKids(String val) {
        if (!val.isEmpty()) LovelifeWantKids = KidsPreference.valueOf(val).name();
    }

    public boolean LovelifeAlreadyHaveChildren = false;
    public boolean getLovelifeAlreadyHaveChildren() {
        return LovelifeAlreadyHaveChildren;
    }
    public void setLovelifeAlreadyHaveChildren(boolean val) {
        LovelifeAlreadyHaveChildren = val;
    }

    public boolean LovelifeWillingToRelocate = false;
    public boolean getLovelifeWillingToRelocate() {
        return LovelifeWillingToRelocate;
    }
    public void setLovelifeWillingToRelocate(boolean val) {
        LovelifeWillingToRelocate = val;
    }



    public Profile() {}
    public Profile(long userid) {
        this.ID = userid;
        Write();
    }

    public static Profile getById(long id) {
        return DatabaseObject.getById(Profile.class, id).orElse(null);
    }

    public List<Profile_Photo> getPhotos() {
        return photos == null ? photos = Profile_Photo.getByUser(ID) : photos;
    }


    public static class Profile_View extends Profile {
        public String Username;
        public String Email;
        public Long Age;

        public Profile_View() {}

        public static Profile_View getView(long id) {
            return DatabaseObject.getById(Profile_View.class, id).orElse(null);
        }
        public static Profile_View getView(String username) {
            return DatabaseObject.getWhere(Profile_View.class, "Username = ?", username).orElse(null);
        }

        public static List<Profile_View> search(long searchId, int page) {
            return DatabaseObject.doQueryAll(Profile_View.class,"CALL MatchUsers(?,?,?);", searchId, page, 100);
        }
    }

    public static class Profile_Edit extends Profile_View {
        public Long NullFields;

        public Profile_Edit() {}

        public static Profile_Edit getEdit(String username) {
            return DatabaseObject.doQuery(Profile_Edit.class,"CALL FetchUserdata(?,?,?);", "profile_view", "Username", username).orElse(null);
        }
    }
}
