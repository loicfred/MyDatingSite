package my.dating.app.object;

import my.utilities.db.DBSaver;
import my.utilities.db.DatabaseEditor;

import java.sql.SQLException;
import java.util.List;

import static my.dating.app.MyDatingSiteApplication.DBM;

public class Search_Profile implements DBSaver<Search_Profile> {
    public final transient DatabaseEditor<Search_Profile> DBE = new DatabaseEditor<>(DBM,this, Search_Profile.class);

    public long ID;
    public String Name;
    public void setName(String name) {
        Name = DBE.AddSet("Name", name);
    }

    public int MinAge;
    public int MaxAge;
    public void setMinAge(int minAge) {
        MinAge = DBE.AddSet("MinAge", minAge);
    }
    public void setMaxAge(int maxAge) {
        MaxAge = DBE.AddSet("MaxAge", maxAge);
    }

    public String Gender;
    public void setGender(String gender) {
        Gender = DBE.AddSet("Gender", gender);
    }


    public static Search_Profile getById(long id) {
        return DBM.retrieveItems(Search_Profile.class).where("ID = ?", id).mapFirstTo(Search_Profile.class);
    }
    public static List<Search_Profile> getByUser(User user) {
        return DBM.retrieveItems(Search_Profile.class).where("UserID = ?", user.getId()).mapAllTo(Search_Profile.class);
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
    public Search_Profile Write() throws SQLException {
        return DBE.Write(false, true);
    }
}
