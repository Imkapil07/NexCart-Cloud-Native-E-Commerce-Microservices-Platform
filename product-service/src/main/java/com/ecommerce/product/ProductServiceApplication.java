package com.ecommerce.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

/** Auth is at API Gateway (JWT). This service only checks X-User-Id for write ops. No default generated password. */
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class ProductServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(ProductServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady(ApplicationReadyEvent event) {
		Environment env = event.getApplicationContext().getEnvironment();
		String port = env.getProperty("server.port", "8085");
		log.info("[ProductService] ========================================");
		log.info("[ProductService] Product service is UP on port {}. Call via API Gateway (port 8080) with JWT for write operations.", port);
		log.info("[ProductService] ========================================");
	}
}
