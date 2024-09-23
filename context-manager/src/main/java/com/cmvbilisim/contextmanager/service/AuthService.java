package com.cmvbilisim.contextmanager.service;

import com.cmvbilisim.contextmanager.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final RestTemplate restTemplate;

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.client-secret}")
    private String clientSecret;

    @Value("${auth.token-url}")
    private String tokenUrl;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            logger.info("Requesting token for user: {}", username);
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Token successfully retrieved for user: {}", username);
                return response.getBody();
            } else {
                logger.error("Token retrieval failed for user: {} with status code: {}", username, response.getStatusCode());
                throw new AuthException("Token retrieval failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error fetching token for user: {}", username, e);
            throw new AuthException("Error fetching token: " + e.getMessage());
        }
    }
}
