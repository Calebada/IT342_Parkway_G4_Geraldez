package com.parkway.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Get frontend URL from environment variable or use defaults
        String frontendUrl = System.getenv("FRONTEND_URL") != null ? 
            System.getenv("FRONTEND_URL") : "http://localhost:3000";
        
        List<String> allowedOrigins = List.of(
            "http://localhost:3000", 
            "http://localhost:3001", 
            "http://127.0.0.1:3000",
            frontendUrl
        );
        
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(allowedOrigins);
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setExposedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(false);
                    corsConfig.setMaxAge(3600L);
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/public/**").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}