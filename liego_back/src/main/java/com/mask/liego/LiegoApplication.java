package com.mask.liego;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class LiegoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiegoApplication.class, args);
    }

}
