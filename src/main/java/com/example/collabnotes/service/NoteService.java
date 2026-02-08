package com.example.collabnotes.service;

import com.example.collabnotes.controller.AuthController;
import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.entity.Note;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.NoteResponseMapper;
import com.example.collabnotes.repository.NoteRepository;
import com.example.collabnotes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteResponseMapper noteResponseMapper;

    public List<NoteResponse> getAllNotes(Long userId) {

        return (userId != null ? noteRepository.findAllByCreatedBy_Id(userId) : noteRepository.findAll()).stream()
                .map(noteResponseMapper::map)
                .toList();
    }

    public NoteResponse getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note with id: " + noteId + " not found"));

        return noteResponseMapper.map(note);
    }

    public NoteResponse createNote(Note note) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + authentication.getName() + " not found"));

        Note noteToCreate = Note.builder()
                .title(note.getTitle())
                .content(note.getContent())
                .createdBy(user)
                .build();

        Note noteCreated = noteRepository.save(noteToCreate);
        return noteResponseMapper.map(noteCreated);
    }

    public void deleteNoteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}
