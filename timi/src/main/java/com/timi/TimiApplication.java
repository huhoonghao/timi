package com.timi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication(scanBasePackages ="com.timi")
public class TimiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimiApplication.class, args);
    }

}
