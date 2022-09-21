package com.finalproject.seatudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SeaTudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeaTudyApplication.class, args);
    }

}
