package com.example.demo;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlantNewApplication {

	public static void main(String[] args) {
		new File(PlantController.uploadDirectory).mkdir();
		SpringApplication.run(PlantNewApplication.class, args);
	}

}
