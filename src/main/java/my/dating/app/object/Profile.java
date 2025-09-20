package my.dating.app.object;

import my.dating.app.object.enums.*;
import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;
import my.utilities.db.QueryParameter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Profile implements DBSaver<Profile> {
    private final transient DatabaseEditor<Profile> DBE = new DatabaseEditor<>(DBM,this, Profile.class);
    private transient List<Profile_Photo> photos;

    public transient Set<Interest> interests;
    public Set<Interest> getInterests() {
        return interests == null ? interests = DBM.retrieveItems("profile_to_interest").where("UserID = ?", ID).select("Value").get().stream().map(TR -> Interest.valueOf(TR.getAsString("Value"))).collect(Collectors.toSet()) : interests;
    }
    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
        DBM.deleteItems("profile_to_interest").where("UserID = ?", ID).delete();
        for (Interest I : interests)
            DBM.createItem("profile_to_interest").create(new QueryParameter("UserID", ID), new QueryParameter("Value", I.name()));
    }

    public transient Set<Language> languages;
    public Set<Language> getLanguages() {
        return languages == null ? languages = DBM.retrieveItems("profile_to_language").where("UserID = ?", ID).select("Value").get().stream().map(TR -> Language.valueOf(TR.getAsString("Value"))).collect(Collectors.toSet()) : languages;
    }
    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
        DBM.deleteItems("profile_to_language").where("UserID = ?", ID).delete();
        for (Language I : languages)
            DBM.createItem("profile_to_language").create(new QueryParameter("UserID", ID), new QueryParameter("Value", I.name()));
    }

    public long ID = 0;
    public long getID() {
        return ID;
    }
    public void setID(long id) {
        ID = DBE.AddSet("ID", id);
    }

    public String Name = "New User";
    public String getName() {
        return Name;
    }
    public void setName(String val) {
        Name = DBE.AddSet("Name", val);
    }

    public LocalDate DateOfBirth;
    public LocalDate getDateOfBirth() {
        return DateOfBirth;
    }
    public void setDateOfBirth(LocalDate val) {
        if (val != null) DateOfBirth = DBE.AddSet("DateOfBirth", val);
    }

    public String Gender = "Male";
    public String getGender() {
        return Gender;
    }
    public void setGender(String val) {
        if (!val.isEmpty()) Gender = DBE.AddSet("Gender", my.dating.app.object.enums.Gender.valueOf(val).name());
    }

    public String WhoAmI;
    public String getWhoAmI() {
        return WhoAmI;
    }
    public void setWhoAmI(String val) {
        if (!val.isEmpty()) WhoAmI = DBE.AddSet("WhoAmI", val);
    }

    public String WhatIWant;
    public String getWhatIWant() {
        return WhatIWant;
    }
    public void setWhatIWant(String val) {
        if (!val.isEmpty()) WhatIWant = DBE.AddSet("WhatIWant", val);
    }

    public String WhatIDislike;
    public String getWhatIDislike() {
        return WhatIDislike;
    }
    public void setWhatIDislike(String val) {
        if (!val.isEmpty()) WhatIDislike = DBE.AddSet("WhatIDislike", val);
    }

    public String PersonalityType;
    public String getPersonalityType() {
        return PersonalityType;
    }
    public void setPersonalityType(String val) {
        if (!val.isEmpty()) PersonalityType = DBE.AddSet("PersonalityType", my.dating.app.object.enums.PersonalityType.valueOf(val).name());
    }

    public String Occupation;
    public String getOccupation() {
        return Occupation;
    }
    public void setOccupation(String val) {
        if (!val.isEmpty()) Occupation = DBE.AddSet("Occupation", val);
    }

    public String Education;
    public String getEducation() {
        return Education;
    }
    public void setEducation(String val) {
        if (!val.isEmpty()) Education = DBE.AddSet("Education", val);
    }

    public String Religion;
    public String getReligion() {
        return Religion;
    }
    public void setReligion(String val) {
        if (!val.isEmpty()) Religion = DBE.AddSet("Religion", my.dating.app.object.enums.Religion.valueOf(val).name());
    }

    public String WouldCook;
    public String getWouldCook() {
        return WouldCook;
    }
    public void setWouldCook(String val) {
        if (!val.isEmpty()) WouldCook = DBE.AddSet("WouldCook", LifestylePreference.valueOf(val).name());
    }

    public String WouldChore;
    public String getWouldChore() {
        return WouldChore;
    }
    public void setWouldChore(String val) {
        if (!val.isEmpty()) WouldChore = DBE.AddSet("WouldChore", LifestylePreference.valueOf(val).name());
    }

    public String StatusSmoking;
    public String getStatusSmoking() {
        return StatusSmoking;
    }
    public void setStatusSmoking(String val) {
        if (!val.isEmpty()) StatusSmoking = DBE.AddSet("SmokingStatus", BadHabit.valueOf(val).name());
    }

    public String StatusDrinking;
    public String getStatusDrinking() {
        return StatusDrinking;
    }
    public void setStatusDrinking(String val) {
        if (!val.isEmpty()) StatusDrinking = DBE.AddSet("StatusDrinking", BadHabit.valueOf(val).name());
    }

    public String StatusExercise;
    public String getStatusExercise() {
        return StatusExercise;
    }
    public void setStatusExercise(String val) {
        if (!val.isEmpty()) StatusExercise = DBE.AddSet("StatusExercise", BadHabit.valueOf(val).name());
    }

    public String Allergies;
    public String getAllergies() {
        return Allergies;
    }
    public void setAllergies(String val) {
        if (!val.isEmpty()) Allergies = DBE.AddSet("Allergies", val);
    }

    public String Disability;
    public String getDisability() {
        return Disability;
    }
    public void setDisability(String val) {
        if (!val.isEmpty()) Disability = DBE.AddSet("Disability", val);
    }

    public boolean WantMarriage = true;
    public boolean getWantMarriage() {
        return WantMarriage;
    }
    public void setWantMarriage(boolean val) {
        WantMarriage = DBE.AddSet("WantMarriage", val);
    }

    public boolean ReligionMatter = false;
    public boolean getReligionMatter() {
        return ReligionMatter;
    }
    public void setReligionMatter(boolean val) {
        ReligionMatter = DBE.AddSet("ReligionMatter", val);
    }

    public String WantKids;
    public String getWantKids() {
        return WantKids;
    }
    public void setWantKids(String val) {
        if (!val.isEmpty()) WantKids = DBE.AddSet("WantKids", KidsPreference.valueOf(val).name());
    }

    public String Diet;
    public String getDiet() {
        return Diet;
    }
    public void setDiet(String val) {
        if (!val.isEmpty()) Diet = DBE.AddSet("Diet", val);
    }

    public boolean LikePets = false;
    public boolean getLikePets() {
        return LikePets;
    }
    public void setLikePets(boolean val) {
        LikePets = DBE.AddSet("LikePets", val);
    }

    public boolean AlreadyHaveChildren = false;
    public boolean getAlreadyHaveChildren() {
        return AlreadyHaveChildren;
    }
    public void setAlreadyHaveChildren(boolean val) {
        AlreadyHaveChildren = DBE.AddSet("AlreadyHaveChildren", val);
    }

    public boolean WillingToRelocate = false;
    public boolean getWillingToRelocate() {
        return WillingToRelocate;
    }
    public void setWillingToRelocate(boolean val) {
        WillingToRelocate = DBE.AddSet("WillingToRelocate", val);
    }

    public boolean Virgin = false;
    public boolean getVirgin() {
        return Virgin;
    }
    public void setVirgin(boolean val) {
        Virgin = DBE.AddSet("Virgin", val);
    }

    public int PastPartners = 0;
    public int getPastPartners() {
        return PastPartners;
    }
    public void setPastPartners(int val) {
        PastPartners = DBE.AddSet("PastPartners", Math.max(0,val));
    }

    public boolean MessageRead = false;
    public boolean getMessageRead() {
        return MessageRead;
    }
    public void setMessageRead(boolean val) {
        MessageRead = DBE.AddSet("MessageRead", val);
    }

    public String MessageReplySpeed;
    public String getMessageReplySpeed() {
        return MessageReplySpeed;
    }
    public void setMessageReplySpeed(String val) {
        if (!val.isEmpty()) MessageReplySpeed = DBE.AddSet("MessageReplySpeed", Rapidity.valueOf(val).name());
    }

    public double Latitude = 0;
    public double Longitude = 0;
    public double getLatitude() {
        return Latitude;
    }
    public double getLongitude() {
        return Longitude;
    }
    public void setLatitude(double val) {
        Latitude = DBE.AddSet("Latitude", val);
    }
    public void setLongitude(double val) {
        Longitude = DBE.AddSet("Longitude", val);
    }


    public byte[] Avatar = null;
    public void setAvatar(byte[] avatar) {
        Avatar = DBE.AddSet("Avatar", avatar);
    }


    public Profile() {}
    public Profile(long userid) throws SQLException {
        this.ID = userid;
        Write();
    }

    public static Profile getById(long id) {
        return DBM.retrieveItems(Profile.class).where("ID = ?", id).mapFirstTo(Profile.class);
    }

    public List<Profile_Photo> getPhotos() {
        return photos == null ? photos = Profile_Photo.getByUser(ID) : photos;
    }

    @Override
    public int Update() throws SQLException {
        return DBE.Update("ID = ?", ID);
    }

    @Override
    public int Delete() throws SQLException {
        return DBE.Delete("ID = ?", ID);
    }

    @Override
    public Profile Write() throws SQLException {
         return DBE.Write(false, true);
    }

    public static class Profile_View extends Profile {
        public String Username;
        public String Email;
        public Long Age;

        public Profile_View() {}

        public static Profile_View getView(long id) {
            return DBM.retrieveItems(Profile_View.class).where("ID = ?", id).mapFirstTo(Profile_View.class);
        }
        public static Profile_View getView(String username) {
            return DBM.retrieveItems(Profile_View.class).where("Username = ?", username).mapFirstTo(Profile_View.class);
        }

        public static List<Profile_View> search(long searchId, int page) throws SQLException {
            return DBM.processQuery("CALL MatchUsers(?,?,?);", searchId, page, 100).stream().map(row -> row.mapTo(Profile_View.class)).collect(Collectors.toList());
        }
    }

    public static class Profile_Edit extends Profile_View {
        public Long NullFields;

        public Profile_Edit() {}

        public static Profile_Edit getEdit(String username) throws SQLException {
            return DBM.processQuery("CALL FetchUserdata(?,?,?);", "profile_view", "Username", username).get(0).mapTo(Profile_Edit.class);
        }
    }
}
