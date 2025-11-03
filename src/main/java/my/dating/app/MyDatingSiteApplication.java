package my.dating.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyDatingSiteApplication {
    public static Preferences Prefs = Preferences.Load();

    public static void main(String[] args) {
        SpringApplication.run(MyDatingSiteApplication.class, args);

    }

}