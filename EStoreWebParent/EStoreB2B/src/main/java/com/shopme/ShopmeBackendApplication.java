package com.shopme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity"})
public class ShopmeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeBackendApplication.class, args);
    }

}
