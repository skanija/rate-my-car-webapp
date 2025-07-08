package com.example.rate_my_car;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.rate_my_car.mvc.storage.StorageService;

@SpringBootApplication
public class RateMyCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateMyCarApplication.class, args);
	}

	@Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}
