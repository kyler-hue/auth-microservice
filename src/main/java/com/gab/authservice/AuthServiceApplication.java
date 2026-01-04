package com.gab.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class AuthServiceApplication {

	// mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

	public static void main(String[] args) {
		// Load .env file for local development BEFORE Spring Boot starts
		// This ensures environment variables are available during Spring initialization
		boolean isProd = isProductionMode(args);

		if (!isProd) {
			// Local development mode - load .env file
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dotenv.entries().forEach(e ->
					System.setProperty(e.getKey(), e.getValue())
			);

			System.out.println("dotenv directory = " + System.getProperty("user.dir"));
			System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME", ""));
			System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD", ""));
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET", ""));
			System.setProperty("PAT_TOKEN", dotenv.get("PAT_TOKEN", ""));
			System.out.println("DB_USERNAME: "+dotenv.get("DB_USERNAME", ""));
			System.out.println("DB_PASSWORD: "+dotenv.get("DB_PASSWORD", ""));
			System.out.println("Local development mode: .env file loaded");
		} else {
			System.out.println("Production mode: Using AWS Secrets Manager");
		}
		
		SpringApplication.run(AuthServiceApplication.class, args);
	}
	
	private static boolean isProductionMode(String[] args) {
		// Check if production profile is explicitly set
		for (String arg : args) {
			if (arg.contains("spring.profiles.active=prod")) {
				return true;
			}
		}
		// Check system property
		String activeProfiles = System.getProperty("spring.profiles.active");
		return activeProfiles != null && activeProfiles.contains("prod");
	}

}
