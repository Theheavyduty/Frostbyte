package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Security configuration for the reactive Spring Cloud Gateway (WebFlux).
 * - No servlet API, so we use WebFlux security.
 * - Gateway does NOT handle auth; it just proxies to user-service.
 */
@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                // Disable CSRF for simple API usage
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // We already configured CORS via spring.cloud.gateway.globalcors in YAML,
                // so we don't need extra CORS handling here.
                .cors(ServerHttpSecurity.CorsSpec::disable)

                // Disable all built-in auth mechanisms on the gateway
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                // Allow everything to pass through; user-service does the real security
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                );

        return http.build();
    }
}