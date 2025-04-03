package com.BankOnlineApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info =@Info(title="Bank App",description="Backend REST API BANK Application"))
public class BankOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankOnlineApplication.class, args);
	}

}
