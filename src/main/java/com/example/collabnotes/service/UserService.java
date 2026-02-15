package com.example.collabnotes.service;

import com.example.collabnotes.dto.RegistrationRequest;
import com.example.collabnotes.dto.UpdateUserRequest;
import com.example.collabnotes.dto.UserResponse;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.CreateUserRequestMapper;
import com.example.collabnotes.mapper.UserResponseMapper;
import com.example.collabnotes.repository.UserRepository;
import com.example.collabnotes.security.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final CurrentUserProvider currentUserProvider;

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

    @Transactional
    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {

        Long userId = currentUserProvider.requireUserId();


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found"));
        if (updateUserRequest.getEmail() != null) {
            userRepository.findByEmail(updateUserRequest.getEmail())
                    .filter(existing -> !existing.getId()
                            .equals(userId))
                    .ifPresent(existing -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already taken");
                    });

            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        userRepository.save(user);

        return userResponseMapper.map(user);
    }


}
