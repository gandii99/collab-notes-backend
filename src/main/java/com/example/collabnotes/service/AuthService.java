package com.example.collabnotes.service;

import com.example.collabnotes.dto.LoginRequest;
import com.example.collabnotes.dto.LoginResponse;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.repository.UserRepository;
import com.example.collabnotes.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }

}
