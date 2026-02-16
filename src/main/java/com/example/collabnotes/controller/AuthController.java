package com.example.collabnotes.controller;

import com.example.collabnotes.dto.LoginRequest;
import com.example.collabnotes.dto.RegistrationRequest;
import com.example.collabnotes.dto.UserResponse;
import com.example.collabnotes.security.CurrentUserProvider;
import com.example.collabnotes.service.AuthService;
import com.example.collabnotes.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CurrentUserProvider currentUserProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        return authService.login(request, response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request, HttpServletResponse response) {
        return authService.registration(request, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/me")
    public UserResponse me() {
        Long userId = currentUserProvider.requireUserId();
        return userService.getUserById(userId);
//        return SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getName();
    }
}
