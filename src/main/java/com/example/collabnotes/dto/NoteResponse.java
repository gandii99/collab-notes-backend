package com.example.collabnotes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private Long createdBy;
    private String createdAt;

}
