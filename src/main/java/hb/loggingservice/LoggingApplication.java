package hb.loggingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LoggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggingApplication.class, args);
    }

}
