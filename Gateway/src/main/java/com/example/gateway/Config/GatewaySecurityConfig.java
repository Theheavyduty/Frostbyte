package com.example.gateway.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for the reactive Spring Cloud Gateway.
 * Configured to allow all traffic through - authentication is handled by user-service.
 */
@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // Disable CSRF - not needed for API gateway
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Disable HTTP Basic authentication (no popup!)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // Disable form login (no popup!)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Allow all requests through - user-service handles auth
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                );

        return http.build();
    }
}