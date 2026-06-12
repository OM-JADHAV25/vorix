package com.vorix.authservice.security.filter;

import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends  OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (!StringUtils.hasText(header) ||
                !header.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {

            if (!jwtService.isTokenValid(token)) {

                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.extractTokenType(token)
                    != JwtTokenType.ACCESS) {

                filterChain.doFilter(request, response);
                return;
            }

            var authorities = jwtService.extractRoles(token)
                                        .stream()
                                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                        .collect(Collectors.toUnmodifiableSet());

            var authentication = new UsernamePasswordAuthenticationToken(
                                 jwtService.extractUserId(token),
                       null,
                                 authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception ex) {

            log.debug("JWT authentication failed: {}", ex.getMessage());

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
