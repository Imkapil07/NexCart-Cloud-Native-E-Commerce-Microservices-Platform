package com.ecommerce.product.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Ensures protected write operations are called via API Gateway (which sets X-User-Id after JWT validation).
 * Logs every request. Returns 401 if POST/PUT/DELETE on /api/categories or /api/products without X-User-Id.
 */
@Component
@Order(1)
public class GatewayAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(GatewayAuthFilter.class);
    private static final String X_USER_ID = "X-User-Id";
    private static final List<String> PROTECTED_PATHS = List.of("/api/categories", "/api/products");
    private static final List<String> WRITE_METHODS = List.of("POST", "PUT", "DELETE");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String method = request.getMethod();
        String path = request.getRequestURI();
        log.info("[ProductService] Request: {} {}", method, path);

        boolean isProtectedWrite = WRITE_METHODS.contains(method)
                && PROTECTED_PATHS.stream().anyMatch(path::startsWith);
        if (isProtectedWrite) {
            String userId = request.getHeader(X_USER_ID);
            if (userId == null || userId.isBlank()) {
                log.warn("[ProductService] 401 {} {}: missing X-User-Id. Call via API Gateway (e.g. http://localhost:8080) with Authorization: Bearer <token>", method, path);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized. Use API Gateway (port 8080) with Bearer token from POST /api/users/login.\"}");
                return;
            }
            log.info("[ProductService] X-User-Id={} -> allowing {} {}", userId, method, path);
        }

        filterChain.doFilter(request, response);
    }
}
