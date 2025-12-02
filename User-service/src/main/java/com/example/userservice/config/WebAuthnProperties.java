package com.example.userservice.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
@Getter @Setter
@ConfigurationProperties(prefix = "app.webauthn")
public class WebAuthnProperties {

    private String rpId;
    private String rpName;
    private List<String> allowedOrigins;
}
