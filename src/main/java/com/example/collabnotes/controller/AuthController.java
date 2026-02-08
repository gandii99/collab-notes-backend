package com.example.collabnotes.controller;

import com.example.collabnotes.dto.LoginRequest;
import com.example.collabnotes.dto.LoginResponse;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("me")
    public String me() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
