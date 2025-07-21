package com.example.doping_case;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Bu annotasyonun burada olması kritik
public class DopingCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DopingCaseApplication.class, args);
    }

}