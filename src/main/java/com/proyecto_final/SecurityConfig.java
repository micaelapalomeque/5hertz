package com.proyecto_final;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // 🔸 Desactiva CSRF (necesario para POST desde fuera del navegador)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // ✅ Permite todas las solicitudes
            );

        return http.build();
    }
}
