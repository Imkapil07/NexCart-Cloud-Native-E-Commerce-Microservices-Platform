package com.ecommerce.user.util;

import com.ecommerce.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility for generating and validating JWT tokens
 * (subject=userId, claims: email, role).
 **/
@Component
public class JwtUtil {

    private final UserRepository userRepository;
    private final SecretKey key;
    private final long jwtExpirationMs;
    public JwtUtil(@Value("${app.jwt.secret}") String Secret, @Value("${app.jwt.expiration-ms}") long jwtExpirationMs, UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(Secret.getBytes());
        this.jwtExpirationMs =jwtExpirationMs;
        this.userRepository = userRepository;
    }
    /** Builds a signed JWT with subject=userId and claims email, role; expiration from config. */
    public String generateToken(Long userId, String email, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtExpirationMs)).signWith(key)
                .compact();
    }
    /**
     * Validates signature and returns parsed claims;
     * throws if invalid or expired.
     **/
    public Jws<Claims> validateToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    /**
     * Extracts user id from token subject.
     **/
    public Long getUserIdFromToken(String token) {
        Claims claims = validateToken(token).getBody();
        return Long.valueOf(claims.getSubject());
    }
}
