package com.cognixia.jump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		// provides meta data on the API service
		info = @Info(
					title = "Pokémon trainer(user) management system", // title of the Documentation page
					version = "1.0",	// version of your API
					description = "API that allows you to login and that allows you to get pokemon data and build pokemon team",
					contact = @Contact(name = "Ethan Shin", email = "jimvic37@gmail.com", url = "https://www.collabera.com"),
					license = @License(name = "Pokemon License v1.0", url = "https://www.collabera.com/"),
					termsOfService = "https://www.collabera.com/" // must be a url
				),
		externalDocs = @ExternalDocumentation(description = "More info on the Pokemon API", url = "https://pokeapi.co//")
)
public class CapstoneProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneProjectApplication.class, args);
	}
}