package com.skills.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceApplication.class);

	public static void main(String[] args) {
        
        try {
			SpringApplication.run(UserServiceApplication.class, args);
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg != null) {
				log.error("FAILED TO START: " + e.getMessage());
				if (msg.contains("Could not resolve placeholder")) {
					log.info("**** This is due to a missing environment variable. Setup the environment variable in the shells login script and re-login or change application.yml. ****");
				}
			}
		}	
    }
}
