package com.znet.logging.demo.friends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.znet.logging.demo")
public class FriendsApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsApiServiceApplication.class, args);
	}
}
