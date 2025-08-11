package org.example.storagesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StorageSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageSystemApplication.class, args);
    }
}
