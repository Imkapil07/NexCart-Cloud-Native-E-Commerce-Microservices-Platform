package com.ecommerce.api.security;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(JwtGatewayFilter.class);
    @Autowired
    private JwtUtil jwtUtil;
    private static final List<String> PUBLIC_URLS = List.of(
            "/api/users/register",
            "/api/users/login",
            "/products/images"
    );
    @Override
    public int getOrder() {
        return -1;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        log.info("[Gateway] Request: {} {} -> {}", method, path, exchange.getRequest().getURI().getHost());

        if (PUBLIC_URLS.stream().anyMatch(path::startsWith)) {
            log.info("[Gateway] Public path - no auth required: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            log.warn("[Gateway] 401 {} {}: missing Authorization header", method, path);
            return unauthorized(exchange);
        }
        authHeader = authHeader.trim();
        if (!authHeader.toLowerCase().startsWith("bearer ")) {
            log.warn("[Gateway] 401 {} {}: Authorization header must be 'Bearer <token>' (got scheme: {})", method, path, authHeader.length() > 10 ? authHeader.substring(0, 10) + "..." : authHeader);
            return unauthorized(exchange);
        }
        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            log.warn("[Gateway] 401 {} {}: token is empty after 'Bearer '", method, path);
            return unauthorized(exchange);
        }
        if (!jwtUtil.validateToken(token)) {
            log.warn("[Gateway] 401 {} {}: JWT validation failed - check JwtUtil logs above for reason", method, path);
            return unauthorized(exchange);
        }
        String role = jwtUtil.getRole(token);
        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        if (!isAuthorized(role, path, method)) {
            log.warn("[Gateway] 403 {} {}: role {} not allowed for this path/method", method, path, role != null ? role : "null");
            return forbidden(exchange);
        }
        String userId = jwtUtil.getUserId(token);
        log.info("[Gateway] Auth OK: userId={} role={} -> forwarding {} {}", userId, role, method, path);
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header("X-User-Id", userId).header("X-User-Role", role).build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
    private Mono<Void> unauthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
    private static final Map<String, Map<String, List<String>>> ROLE_API_PERMISSION = Map.of(
            "ADMIN", Map.of(
                    "GET", List.of("/api/products", "/api/categories", "/api/orders", "/api/payments","/api/inventory"),
                    "POST", List.of("/api/products", "/api/categories", "/api/inventory"),
                    "PUT", List.of("/api/products", "/api/categories", "/api/payments"),
                    "DELETE", List.of("/api/products", "/api/categories")
            ),
            "USER", Map.of(
        "GET", List.of("/api/products", "/api/categories", "/api/inventory", "/api/payments", "/api/users"),
        "POST", List.of("/api/categories", "/api/products", "/api/order", "/api/payment")
)
    );
    private boolean isAuthorized(String role, String path, String method) {
        Map<String, List<String>> permission  = ROLE_API_PERMISSION.get(role);
        if(permission == null) return false;
        List<String> allowedPaths = permission.get(method);
        if(allowedPaths==null) return false;
        return allowedPaths.stream().anyMatch(path::startsWith);
    }
    private Mono<Void> forbidden(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}
