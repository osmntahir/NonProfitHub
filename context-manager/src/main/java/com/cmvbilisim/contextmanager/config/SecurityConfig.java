package com.cmvbilisim.contextmanager.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.Collections;

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
                // Enable CORS with the custom configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authz -> authz
                        // Permit authentication endpoints
                        .requestMatchers("/api/auth/token", "/api/auth/**").permitAll()
                        // Permit public news and announcement endpoints
                        .requestMatchers("/api/news/valid", "/api/news/{id}").permitAll()
                        .requestMatchers("/api/announcement/valid", "/api/announcement/{id}").permitAll()
                        // Protect news and announcement endpoints with ADMIN role
                        .requestMatchers("/api/news/**").hasRole("ADMIN")
                        .requestMatchers("/api/announcement/**").hasRole("ADMIN")
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint((request, response, authException) ->
                                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
                                )
                                .accessDeniedHandler((request, response, accessDeniedException) ->
                                        response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied")
                                )
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // Define a CorsConfigurationSource bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow both localhost and 127.0.0.1 with specified port
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173",
                "http://localhost:5173","http://127.0.0.1:5174","http://localhost:5174"));

        // Allow specific HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        // Allow credentials (cookies, authorization headers, etc.)
        configuration.setAllowCredentials(true);

        // Set the max age to cache preflight response
        configuration.setMaxAge(3600L);

        // Apply the configuration to all API endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
