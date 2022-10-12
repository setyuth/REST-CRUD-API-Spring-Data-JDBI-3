package com.yuthads.springdatajdbi3crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringDataJdbi3CrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJdbi3CrudApplication.class, args);
	}

}
