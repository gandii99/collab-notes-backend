package com.example.collabnotes.dto;

import lombok.Data;

@Data
public class CreateNoteRequest {
    String title;
    String content;
}
