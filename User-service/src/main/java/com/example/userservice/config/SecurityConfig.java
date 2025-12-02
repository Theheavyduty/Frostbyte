package com.example.userservice.config;

import com.example.userservice.repository.EmployeeRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final WebAuthnProperties webAuthnProperties;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public SecurityConfig(WebAuthnProperties webAuthnProperties,
                          UserRepository userRepository,
                          EmployeeRepository employeeRepository) {
        this.webAuthnProperties = webAuthnProperties;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/api/**",
                                "/profile-pictures/**",
                                "/h2-console/**"
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/**",
                                "/api/parents/**",
                                "/api/employees/**",
                                "/webauthn/**",
                                "/profile-pictures/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .webAuthn(webAuthn -> webAuthn
                        .rpId(webAuthnProperties.getRpId())
                        .rpName(webAuthnProperties.getRpName())
                        .allowedOrigins(webAuthnProperties.getAllowedOrigins().toArray(new String[0]))
                );

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 1) Try employee
            var employeeOpt = employeeRepository.findByName(username);
            if (employeeOpt.isPresent()) {
                var e = employeeOpt.get();
                return org.springframework.security.core.userdetails.User
                        .withUsername(e.getName())
                        .password(e.getPassword())
                        .roles("EMPLOYEE")
                        .build();
            }

            // 2) Try child user
            var userOpt = userRepository.findByName(username);
            if (userOpt.isPresent()) {
                var u = userOpt.get();
                return org.springframework.security.core.userdetails.User
                        .withUsername(u.getName())
                        .password(u.getPassword())
                        .roles("USER")
                        .build();
            }

            throw new UsernameNotFoundException("User not found: " + username);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
