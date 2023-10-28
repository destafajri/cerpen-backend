package com.backend.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class JavaApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JavaApplication.class);
		String port = System.getenv("PORT");
		app.setDefaultProperties(Collections.singletonMap("server.port", port == null ? "8080" : port));
		app.run(args);
	}

}
