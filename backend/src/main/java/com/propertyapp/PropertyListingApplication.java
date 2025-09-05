package com.propertyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PropertyListingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropertyListingApplication.class, args);
    }
}
