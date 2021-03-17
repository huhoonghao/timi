package com.timi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ="com.timi")
public class TimiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimiApplication.class, args);
    }

}
