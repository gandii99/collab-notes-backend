package com.example.collabnotes.dto;

import lombok.Data;

@Data
public class CreateNoteRequest {
    private String title;
    private String content;
}
