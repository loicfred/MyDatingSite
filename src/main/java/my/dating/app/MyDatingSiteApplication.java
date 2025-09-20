package my.dating.app;

import my.dating.app.config.AppConfig;
import my.utilities.db.DatabaseManager;
import my.utilities.db.DatabaseType;
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
        DBM = new DatabaseManager(DatabaseType.MariaDB, Prefs.DatabaseURL, Prefs.DatabaseUsername, Prefs.DatabasePassword, true);
    }

}
