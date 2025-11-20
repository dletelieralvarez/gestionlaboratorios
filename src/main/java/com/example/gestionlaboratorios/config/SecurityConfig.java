package com.example.gestionlaboratorios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar el ejemplo
            .authorizeHttpRequests(auth -> auth 
              //.requestMatchers("/actuator/health").permitAll()
              //.requestMatchers(HttpMethod.GET, "/resultados/usuario/**").hasAnyRole("CLIENTE","TECNICO","ADMIN")
              //.requestMatchers(HttpMethod.POST, "/resultados/**").hasAnyRole("TECNICO","ADMIN")
              //.requestMatchers(HttpMethod.PUT, "/resultados/**").hasAnyRole("TECNICO","ADMIN")
              //.requestMatchers(HttpMethod.DELETE, "/resultados/**").hasRole("ADMIN")
              .anyRequest()
              .permitAll()
            ) 
            .httpBasic(Customizer.withDefaults()) // Habilitar autenticación básica (opcional)
            .build();    
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

