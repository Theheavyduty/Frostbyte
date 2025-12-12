package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public static resources and APIs
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/test.html",
                                "/api/**",
                                "/h2-console/**",
                                "/js/**",
                                "/css/**",
                                "/images/**",
                                "/profile-pictures/**",
                                "/test"
                        ).permitAll()
                        // WebAuthn LOGIN endpoints are public
                        .requestMatchers(
                                "/webauthn/authenticate/**",
                                "/login/webauthn"
                        ).permitAll()
                        // WebAuthn REGISTER requires authentication
                        // (default behavior - no need to explicitly configure)
                        .anyRequest().authenticated()
                )

                // Form login for initial authentication
                .formLogin(form -> form
                        .defaultSuccessUrl("/test.html", true)
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/test.html")
                        .permitAll()
                )

                // WebAuthn / Passkeys
                .webAuthn(webAuthn -> webAuthn
                        .rpId("localhost")
                        .rpName("User Service App")
                        .allowedOrigins("http://localhost:8001")
                )

                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}