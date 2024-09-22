package com.cmvbilisim.contextmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtClaimsConverter jwtAuthConverter;

    public SecurityConfig(JwtClaimsConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/news/valid", "/api/news/{id}").permitAll()
                        .requestMatchers("/api/announcement/valid", "/api/announcement/{id}").permitAll()
                        .requestMatchers("/api/news/**").hasRole("ADMIN")
                        .requestMatchers("/api/announcement/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint((request, response, authException) ->
                                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
                                )  // Send 401 for unauthenticated requests
                                .accessDeniedHandler((request, response, accessDeniedException) ->
                                        response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied")
                                )  // Send 403 for forbidden access
                )
                .csrf(csrf -> csrf.disable())  // Disable CSRF for APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // Stateless sessions

        return http.build();
    }
}
