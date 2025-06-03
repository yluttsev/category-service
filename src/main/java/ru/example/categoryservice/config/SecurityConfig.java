package ru.example.categoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.example.categoryservice.security.JwtAccessAuthenticationProvider;
import ru.example.categoryservice.security.JwtAccessFilter;

import java.util.Map;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public Map<String, HttpMethod> securedEndpoints() {
        return Map.of(
                "/categories", HttpMethod.POST
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   JwtAccessFilter jwtAccessFilter,
                                                   Map<String, HttpMethod> securedEndpoints) throws Exception {
        return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .anonymous(AnonymousConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .authorizeHttpRequests(customizer -> {
                    securedEndpoints.forEach((endpoint, method) -> customizer.requestMatchers(method, endpoint).authenticated());
                    customizer.anyRequest().permitAll();
                })
                .addFilterBefore(jwtAccessFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public ProviderManager authenticationManager(JwtAccessAuthenticationProvider jwtAccessAuthenticationProvider) {
        return new ProviderManager(
                jwtAccessAuthenticationProvider
        );
    }
}
