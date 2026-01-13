package com.umc.nuvibe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NuvibeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NuvibeApplication.class, args);
	}

}
