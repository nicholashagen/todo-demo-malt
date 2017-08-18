package com.znet.logging.demo.todos.config;

import org.springframework.context.annotation.Bean;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

//@Configuration
public class TaskApiConfiguration {

	@Bean
	public IPing ribbonPing() {
		return new PingUrl();
	}

	@Bean
	public IRule ribbonRule() {
		return new AvailabilityFilteringRule();
	}
}