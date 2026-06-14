package com.vorix.gatewayservice.filter;

import com.vorix.gatewayservice.constants.HeaderConstants;
import com.vorix.gatewayservice.security.jwt.JwtService;
import com.vorix.gatewayservice.security.jwt.JwtTokenType;
import com.vorix.gatewayservice.util.PublicEndpointValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final PublicEndpointValidator publicEndpointValidator;
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest()
                              .getURI()
                              .getPath();

        System.out.println("Jwt Filter Invoked for: " + path);

        if (publicEndpointValidator.isPublic(path)) {

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                                    .getHeaders()
                                    .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        if (jwtService.extractTokenType(token) != JwtTokenType.ACCESS) {

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        UUID userId = jwtService.extractUserId(token);

        String email = jwtService.extractEmail(token);

        Set<String> roles = jwtService.extractRoles(token);

        System.out.println("Authenticated User:");
        System.out.println("User ID: " + userId);
        System.out.println("Email: " + email);
        System.out.println("Roles: " + roles);

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(HeaderConstants.USER_ID, userId.toString())
                .header(HeaderConstants.USER_EMAIL, email)
                .header(HeaderConstants.USER_ROLES, String.join(",", roles))
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                                                    .request(mutatedRequest)
                                                    .build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}