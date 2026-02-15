package com.example.collabnotes.mapper;

import com.example.collabnotes.dto.RegistrationRequest;
import com.example.collabnotes.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestMapper {

    public User map(RegistrationRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }

}
