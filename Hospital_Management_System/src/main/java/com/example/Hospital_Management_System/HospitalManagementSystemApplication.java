package com.example.Hospital_Management_System;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(
				title = "Hospital Management System API Documentation",
			 //  description = ""
			//	version = "1.0.0",
		   //	termsOfService = "https://example.com/terms",

				contact = @Contact(
						name = "Sandesh Adhikari",
						email = "sandesh12adk@gmail.com",
						url = "https://www.facebook.com/sandesh.adhikari.3323"
				)
		)
)



@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class HospitalManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementSystemApplication.class, args);
	}

}
