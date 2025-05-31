package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ClientTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientTestApplication.class, args);
	}

}
