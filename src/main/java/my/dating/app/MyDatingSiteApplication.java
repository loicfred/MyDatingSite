package my.dating.app;

import my.utilities.db.DatabaseManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyDatingSiteApplication {
    public static Preferences Prefs = Preferences.Load();
    public static DatabaseManager DBM;
    public static void main(String[] args) {
        SpringApplication.run(MyDatingSiteApplication.class, args);
        try {Class.forName("com.mysql.cj.jdbc.Driver");
            DBM = new DatabaseManager(Prefs.DatabaseURL, Prefs.DatabaseUsername, Prefs.DatabasePassword, true);
            System.out.println("Starting database connectivity...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
