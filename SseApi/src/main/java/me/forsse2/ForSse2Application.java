package me.forsse2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForSse2Application {

    public static void main(String[] args) {
        SpringApplication.run(ForSse2Application.class, args);
    }

}
