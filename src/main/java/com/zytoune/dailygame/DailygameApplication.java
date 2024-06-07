package com.zytoune.dailygame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DailygameApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailygameApplication.class, args);
	}

}
