package com.example.userservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.webauthn")
public class WebAuthnProperties {

    private String rpId;
    private String rpName;
    private List<String> allowedOrigins;
}