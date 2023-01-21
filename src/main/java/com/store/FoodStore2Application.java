package com.store;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.store.service.MunicipalitiesService;
import com.store.service.OrdersService;
import com.store.service.RoleService;
import com.store.uploadingfiles.storage.StorageProperties;
import com.store.uploadingfiles.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FoodStore2Application {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(FoodStore2Application.class, args);
	}

	/**
	 * Hay que quitar esto para poder hacer algunas las pruebas unitarias, no se
	 * porqué
	 */
	@Bean
	CommandLineRunner init(StorageService storageService, RoleService roleService,
			MunicipalitiesService municipalitiesService, OrdersService ordersService) {
		return (args) -> {
			// storageService.deleteAll();
			roleService.init();
			municipalitiesService.init();
			storageService.init();
			ordersService.init();
		};
	}

}
