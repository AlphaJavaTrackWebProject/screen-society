package org.alphatrack.screensociety;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //TODO tells spring not to search for DB remove it later when DB is implemented //
public class ScreenSocietyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScreenSocietyApplication.class, args);
    }

}
