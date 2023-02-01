package com.keduit.bpro77;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Bpro77Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Bpro77Application.class, args);
	}
	
}
