package com.test.mqservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.test")
public class MqServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqServiceApplication.class, args);
	}
}