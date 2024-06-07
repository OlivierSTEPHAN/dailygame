package com.zytoune.dailygame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GeogamrApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeogamrApplication.class, args);
	}

}
