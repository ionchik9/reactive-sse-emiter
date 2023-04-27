package com.sse.emiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EmiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmiterApplication.class, args);
	}

}
