package com.example.collabnotes.service;

import com.example.collabnotes.dto.CreateUserRequest;
import com.example.collabnotes.dto.UserResponse;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.CreateUserRequestMapper;
import com.example.collabnotes.mapper.UserResponseMapper;
import com.example.collabnotes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseMapper userResponseMapper;
    private final CreateUserRequestMapper createUserRequestMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userResponseMapper::map)
                .toList();
    }

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found"));
        return userResponseMapper.map(user);
    }

    public UserResponse createUser(CreateUserRequest createUserRequest) {
        User userToCreate = createUserRequestMapper.map(createUserRequest);
        userToCreate.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        userRepository.save(userToCreate);

        return userResponseMapper.map(userToCreate);
    }

}
