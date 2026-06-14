package com.epfcore.epfcore;

import com.epfcore.epfcore.documentFormulaire.storage.StorageProperties;
import com.epfcore.epfcore.documentFormulaire.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class EpfcoreApplication {

	static void main(String[] args) {
		SpringApplication.run(EpfcoreApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return _ -> storageService.init();
	}

}
