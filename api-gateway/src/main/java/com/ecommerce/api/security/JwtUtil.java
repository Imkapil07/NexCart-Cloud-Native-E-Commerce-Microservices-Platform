package com.ecommerce.api.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final String DEFAULT_SECRET = "myEcommerceJwtSecretKey2026DummySecretForDevOnly12345";

    @Value("${jwt.secret:" + "myEcommerceJwtSecretKey2026DummySecretForDevOnly12345" + "}")
    private String secret;
    private SecretKey key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            secret = DEFAULT_SECRET;
        }
        secret = secret.trim();
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        log.info("JWT key initialized (secret length={})", secret.length());
    }
    private String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
    public Claims extractClaims(String token) {
        token = cleanToken(token);
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            log.warn("JWT validation failed: token is null or empty");
            return false;
        }
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("JWT validation failed: {} - token length={}", e.getMessage(), token.length());
            return false;
        }
    }
    public String getUserId(String token) {
        return extractClaims(token).getSubject();
    }
    public String getRole(String token) {
        return extractClaims(token).get("role",String.class);
    }

}
