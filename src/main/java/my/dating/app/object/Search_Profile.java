package my.dating.app.object;

import my.dating.app.object.enums.*;
import my.dating.app.service.DatabaseObject;

import java.util.List;

public class Search_Profile extends DatabaseObject<Search_Profile> {

    private Search_Profile() {}

    public long ID;
    public String Name;
    public String getName() { return Name; }
    public void setName(String name) {
        Name = name;
    }

    public int MinAge;
    public int MaxAge;
    public int getMinAge() { return MinAge; }
    public int getMaxAge() { return MaxAge; }
    public void setMinAge(int minAge) {
        MinAge = minAge;
    }
    public void setMaxAge(int maxAge) {
        MaxAge = maxAge;
    }

    public String Gender;
    public String getGender() { return Gender;}
    public void setGender(String gender) {
        Gender = gender;
    }

    public transient String interests;
    public String getInterests() {
        return interests;
    }
    public void setInterests(String interests) {
        this.interests = interests;
    }

    public transient String languages;
    public String getLanguages() {
        return languages;
    }
    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public int LocationRadiusKM;
    public int getLocationRadiusKM() {
        return LocationRadiusKM;
    }
    public void setLocationRadiusKM(int locationRadiusKM) {
        LocationRadiusKM = locationRadiusKM;
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
        if (!val.isEmpty()) LifestyleDiet = val;
    }

    public String LifestyleWouldCook;
    public String getLifestyleWouldCook() {
        return LifestyleWouldCook;
    }
    public void setLifestyleWouldCook(String val) {
        if (!val.isEmpty()) LifestyleWouldCook = val;
    }

    public String LifestyleWouldChore;
    public String getLifestyleWouldChore() {
        return LifestyleWouldChore;
    }
    public void setLifestyleWouldChore(String val) {
        if (!val.isEmpty()) LifestyleWouldChore = val;
    }

    public String LifestyleSmoking;
    public String getLifestyleSmoking() {
        return LifestyleSmoking;
    }
    public void setLifestyleSmoking(String val) {
        if (!val.isEmpty()) LifestyleSmoking = val;
    }

    public String LifestyleDrinking;
    public String getLifestyleDrinking() {
        return LifestyleDrinking;
    }
    public void setLifestyleDrinking(String val) {
        if (!val.isEmpty()) LifestyleDrinking = val;
    }

    public String LifestyleExercise;
    public String getLifestyleExercise() {
        return LifestyleExercise;
    }
    public void setLifestyleExercise(String val) {
        if (!val.isEmpty()) LifestyleExercise = val;
    }

    public Boolean LifestyleAllergies;
    public Boolean getLifestyleAllergies() {
        return LifestyleAllergies;
    }
    public void setLifestyleAllergies(boolean val) {
        LifestyleAllergies = val;
    }

    public Boolean LifestyleDisability;
    public Boolean getLifestyleDisability() {
        return LifestyleDisability;
    }
    public void setLifestyleDisability(boolean val) {
        LifestyleDisability = val;
    }

    public Boolean LifestylePets;
    public Boolean getLifestylePets() {
        return LifestylePets;
    }
    public void setLifestylePets(boolean val) {
        LifestylePets = val;
    }


    public Boolean MessageLeaveOnRead;
    public Boolean getMessageLeaveOnRead() {
        return MessageLeaveOnRead;
    }
    public void setMessageLeaveOnRead(Boolean val) {
        MessageLeaveOnRead = val;
    }

    public String MessageReplySpeed;
    public String getMessageReplySpeed() {
        return MessageReplySpeed;
    }
    public void setMessageReplySpeed(String val) {
        if (!val.isEmpty()) MessageReplySpeed = Rapidity.valueOf(val).name();
    }


    public Boolean LovelifeHadPastPartners;
    public Boolean getLovelifeHadPastPartners() {
        return LovelifeHadPastPartners;
    }
    public void setLovelifeHadPastPartners(boolean val) {
        LovelifeHadPastPartners = val;
    }


    public Boolean LovelifeVirginity;
    public Boolean getLovelifeVirginity() {
        return LovelifeVirginity;
    }
    public void setLovelifeVirginity(boolean val) {
        LovelifeVirginity = val;
    }

    public Boolean LovelifeMarriage;
    public Boolean getLovelifeMarriage() {
        return LovelifeMarriage;
    }
    public void setLovelifeMarriage(boolean val) {
        LovelifeMarriage = val;
    }

    public Boolean LovelifeReligionMatter;
    public Boolean getLovelifeReligionMatter() {
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
        if (!val.isEmpty()) LovelifeWantKids = val;
    }

    public Boolean LovelifeMustBeChildless;
    public Boolean getLovelifeMustBeChildless() {
        return LovelifeMustBeChildless;
    }
    public void setLovelifeMustBeChildless(boolean val) {
        LovelifeMustBeChildless = val;
    }

    public Boolean LovelifeWillingToRelocate;
    public Boolean getLovelifeWillingToRelocate() {
        return LovelifeWillingToRelocate;
    }
    public void setLovelifeWillingToRelocate(boolean val) {
        LovelifeWillingToRelocate = val;
    }

    public static Search_Profile getById(long id) {
        return DatabaseObject.getById(Search_Profile.class, id).orElse(null);
    }
    public static List<Search_Profile> getByUser(User user) {
        return DatabaseObject.getAllWhere(Search_Profile.class, "UserID = ?", user.ID);
    }

    public static double kmToLatitude(double km) {
        return km / 111.0;
    }
    public static double kmToLongitude(double km, double latitude) {
        double kmPerDegree = 111.320 * Math.cos(latitude);
        return km / kmPerDegree;
    }
}
