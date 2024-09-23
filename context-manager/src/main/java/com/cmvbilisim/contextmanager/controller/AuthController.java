package com.cmvbilisim.contextmanager.controller;

import com.cmvbilisim.contextmanager.exception.AuthException;
import com.cmvbilisim.contextmanager.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestParam String username, @RequestParam String password) {
        try {
            String token = authService.getToken(username, password);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
