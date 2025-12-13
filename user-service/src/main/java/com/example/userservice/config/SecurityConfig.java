package com.example.userservice.config;

import com.example.userservice.model.Employee;
import com.example.userservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.webauthn.rp-id:localhost}")
    private String rpId;

    @Value("${app.webauthn.rp-name:Frostbyte User Service}")
    private String rpName;

    private final WebAuthnProperties webAuthnProperties;
    private final EmployeeRepository employeeRepository;

    public SecurityConfig(WebAuthnProperties webAuthnProperties,
                          EmployeeRepository employeeRepository) {
        this.webAuthnProperties = webAuthnProperties;
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http


                .csrf(csrf -> csrf.disable())

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
                                "/error",
                                "/api/**"
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

                // Important: return 401 instead of redirecting to /login for API/WebAuthn
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                request -> request.getRequestURI().startsWith("/webauthn/")
                                        || request.getRequestURI().startsWith("/api/")
                        )
                )

                // Form login for initial authentication
                .formLogin(form -> form
                        .defaultSuccessUrl("/test.html", true)
                        .permitAll()
                )


                // Logout – return JSON 200 OK for APIs / SPA
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        // make absolutely sure the user is really logged out
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Logged out successfully\"}");
                            response.getWriter().flush();
                        })
                        .permitAll()
                )


                // WebAuthn / Passkeys
                .webAuthn(webAuthn -> webAuthn
                        .rpId(rpId)
                        .rpName(rpName)
                        .allowedOrigins(webAuthnProperties.getAllowedOrigins().toArray(new String[0]))
                )

                // Allow H2 console frames (dev only)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // In-memory test user
        UserDetails testUser = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        InMemoryUserDetailsManager inMemory = new InMemoryUserDetailsManager(testUser);

        return username -> {
            // 1) If it's the test user, return from in-memory store
            if ("user".equals(username)) {
                return inMemory.loadUserByUsername(username);
            }

            Employee employee = employeeRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // This should be a BCrypt hash
            String passwordHash = employee.getPassword();

            return User.withUsername(employee.getName())
                    .password(passwordHash)
                    .roles("EMPLOYEE")
                    .build();
        };
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
