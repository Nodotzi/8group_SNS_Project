package com.sparta.snsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SnsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsProjectApplication.class, args);
    }

}
