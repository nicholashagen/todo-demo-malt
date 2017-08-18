package com.znet.logging.demo.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.znet.logging.demo")
public class AuthApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApiServiceApplication.class, args);
	}
}
