package com.example.collabnotes.service;

import com.example.collabnotes.dto.LoginRequest;
import com.example.collabnotes.dto.LoginResponse;
import com.example.collabnotes.dto.RegistrationRequest;
import com.example.collabnotes.dto.UserResponse;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.CreateUserRequestMapper;
import com.example.collabnotes.mapper.UserResponseMapper;
import com.example.collabnotes.repository.UserRepository;
import com.example.collabnotes.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CreateUserRequestMapper createUserRequestMapper;
    private final UserResponseMapper userResponseMapper;

    public ResponseEntity<?> login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(user.getId());

        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        return ResponseEntity.ok()
                .build();
    }

    public ResponseEntity<?> registration(RegistrationRequest createUserRequest, HttpServletResponse response) {

        if (userRepository.findByEmail(createUserRequest.getEmail())
                .isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User userToCreate = createUserRequestMapper.map(createUserRequest);
        userToCreate.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        User createdUser = userRepository.save(userToCreate);

        String token = jwtService.generateToken(createdUser.getId());


        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponseMapper.map(createdUser));
    }


}
