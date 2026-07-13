package org.alphatrack.screensociety;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class ScreenSocietyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScreenSocietyApplication.class, args);
    }

}
