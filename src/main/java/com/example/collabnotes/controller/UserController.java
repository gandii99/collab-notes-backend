package com.example.collabnotes.controller;

import com.example.collabnotes.dto.RegistrationRequest;
import com.example.collabnotes.dto.UpdateUserRequest;
import com.example.collabnotes.dto.UserResponse;
import com.example.collabnotes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUsers(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping()
    public UserResponse updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(updateUserRequest);
    }


}
