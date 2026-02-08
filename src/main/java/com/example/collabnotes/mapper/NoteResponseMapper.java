package com.example.collabnotes.mapper;

import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteResponseMapper {

    public NoteResponse map(Note note) {

        if (note == null) {
            return null;
        }

        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdBy(note.getCreatedBy()
                        .getId())
                .createdAt(note.getCreatedAt()
                        .toInstant()
                        .toString())
                .build();
    }
}
