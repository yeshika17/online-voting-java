package com.example.voting.service;

import com.example.voting.model.User;
import com.example.voting.repository.UserRepository;
import com.example.voting.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User register(String username, String password, String role) {
        if(userRepository.findByUsername(username).isPresent()) throw new RuntimeException("User exists");
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        return userRepository.save(u);
    }

    public String login(String username, String password) {
        Optional<User> ou = userRepository.findByUsername(username);
        if(ou.isEmpty()) throw new RuntimeException("Invalid credentials");
        User u = ou.get();
        if(!encoder.matches(password, u.getPassword())) throw new RuntimeException("Invalid credentials");
        return jwtUtil.generateToken(u.getUsername(), u.getRole());
    }
}
