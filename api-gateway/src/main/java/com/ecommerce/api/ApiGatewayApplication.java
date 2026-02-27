package com.ecommerce.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/** Auth: JWT only (Bearer token from custom login). No default generated password. */
@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
@EnableDiscoveryClient
public class ApiGatewayApplication {

	private static final Logger log = LoggerFactory.getLogger(ApiGatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady(Environment env) {
		String port = env.getProperty("server.port", "8080");
		log.info("[Gateway] ========================================");
		log.info("[Gateway] API Gateway is UP. Use http://localhost:{} for ALL API calls.", port);
		log.info("[Gateway] Protected endpoints need: Authorization: Bearer <token> (get token from POST /api/users/login)");
		log.info("[Gateway] Do NOT call product-service (8085) or other services directly for protected APIs.");
		log.info("[Gateway] ========================================");
	}

}
