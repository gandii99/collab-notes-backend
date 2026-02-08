package com.example.collabnotes.mapper;

import com.example.collabnotes.dto.UserResponse;
import com.example.collabnotes.entity.Note;
import com.example.collabnotes.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public UserResponse map(User user) {

        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

}
