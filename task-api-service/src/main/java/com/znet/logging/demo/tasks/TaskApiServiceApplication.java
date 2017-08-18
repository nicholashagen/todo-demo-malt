package com.znet.logging.demo.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.znet.logging.demo")
public class TaskApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApiServiceApplication.class, args);
	}
}
