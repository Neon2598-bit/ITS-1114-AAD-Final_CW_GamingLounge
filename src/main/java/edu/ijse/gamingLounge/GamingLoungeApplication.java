package edu.ijse.gamingLounge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamingLoungeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamingLoungeApplication.class, args);
	}

}
