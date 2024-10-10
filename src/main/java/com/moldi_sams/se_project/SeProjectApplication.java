package com.moldi_sams.se_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SeProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(SeProjectApplication.class, args);
	}
}
