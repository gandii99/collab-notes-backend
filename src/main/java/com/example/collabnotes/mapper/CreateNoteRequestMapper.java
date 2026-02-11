package com.example.collabnotes.mapper;

import com.example.collabnotes.dto.CreateNoteRequest;
import com.example.collabnotes.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class CreateNoteRequestMapper {

    public Note map(CreateNoteRequest request) {
        if (request == null) {
            return null;
        }

        return Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
