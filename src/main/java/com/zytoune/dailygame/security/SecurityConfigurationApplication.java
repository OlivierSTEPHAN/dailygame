package com.zytoune.dailygame.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfigurationApplication {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Enable CORS
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(HttpMethod.GET, "/daily-games/screenshots/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/daily-games/characteristics/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/daily-games/screenshots/score/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/daily-games/characteristics/score/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/daily-games/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/daily-games/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/games").permitAll()
                                .requestMatchers(HttpMethod.GET, "/games/*").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
