package com.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.webauthn.rp-id:localhost}")
    private String rpId;

    @Value("${app.webauthn.rp-name:User Service App}")
    private String rpName;

    private final WebAuthnProperties webAuthnProperties;

    public SecurityConfig(WebAuthnProperties webAuthnProperties) {
        this.webAuthnProperties = webAuthnProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // Disable CORS here - Gateway handles it
                .cors(cors -> cors.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public static resources and APIs
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/test.html",
                                "/api/auth/login",
                                "/api/**",
                                "/h2-console/**",
                                "/js/**",
                                "/css/**",
                                "/images/**",
                                "/profile-pictures/**",
                                "/test",
                                "/error"
                        ).permitAll()
                        // WebAuthn LOGIN endpoints are public
                        .requestMatchers(
                                "/webauthn/authenticate/**",
                                "/login/webauthn"
                        ).permitAll()
                        // WebAuthn REGISTER requires authentication
                        .requestMatchers("/webauthn/register/**").authenticated()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Important: Return 401 instead of redirecting to login for API/WebAuthn endpoints
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                request -> request.getRequestURI().startsWith("/webauthn/") ||
                                        request.getRequestURI().startsWith("/api/")
                        )
                )

                // Form login for initial authentication
                .formLogin(form -> form
                        .defaultSuccessUrl("/test.html", true)
                        .permitAll()
                )

                // Logout - return 200 OK instead of redirect for API/SPA compatibility
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Logged out successfully\"}");
                            response.getWriter().flush();
                        })
                        .permitAll()
                )

                // WebAuthn / Passkeys configuration
                .webAuthn(webAuthn -> webAuthn
                        .rpId(rpId)
                        .rpName(rpName)
                        .allowedOrigins(webAuthnProperties.getAllowedOrigins().toArray(new String[0]))
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