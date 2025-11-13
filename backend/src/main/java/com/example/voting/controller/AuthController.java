package com.example.voting.controller;

import com.example.voting.service.AuthService;
import com.example.voting.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) {
        return authService.register(req.getUsername(), req.getPassword(), req.getRole()==null?"ROLE_VOTER":req.getRole());
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return new TokenResponse(token);
    }

    @Data static class RegisterRequest { private String username; private String password; private String role; }
    @Data static class LoginRequest { private String username; private String password; }
    @Data static class TokenResponse { private String token; public TokenResponse(String token){ this.token=token;} }
}
