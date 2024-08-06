package com.example.ArtGallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtGalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtGalleryApplication.class, args);
	}

}
