package com.example.collabnotes.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;
    private String firstName;
    private String lastName;

    @AssertTrue(message = "At least one field must be updated")
    public boolean isAnyFieldUpdated() {
        return email != null || firstName != null || lastName != null;
    }

}
