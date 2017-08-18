package com.znet.logging.demo.tracing;

import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Component
public class Tracing {

	private static Tracer TRACER;

	public Tracing(Tracer tracer) {
		TRACER = tracer;
	}

	public static Tracer currentTracer() {
		return TRACER;
	}
}
