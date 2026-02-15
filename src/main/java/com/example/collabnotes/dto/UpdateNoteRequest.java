package com.example.collabnotes.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class UpdateNoteRequest {
    private String title;
    private String content;

    @AssertTrue(message = "At least one field must be updated")
    public boolean isAnyFieldProvided() {
        return title != null || content != null;
    }

}
