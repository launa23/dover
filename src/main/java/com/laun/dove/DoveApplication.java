package com.laun.dove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DoveApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoveApplication.class, args);
	}

}
