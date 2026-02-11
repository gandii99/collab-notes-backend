package com.example.collabnotes.service;

import com.example.collabnotes.dto.CreateNoteRequest;
import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.entity.Note;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.CreateNoteRequestMapper;
import com.example.collabnotes.mapper.NoteResponseMapper;
import com.example.collabnotes.repository.NoteRepository;
import com.example.collabnotes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteResponseMapper noteResponseMapper;
    private final CreateNoteRequestMapper createNoteRequestMapper;

    public List<NoteResponse> getAllNotes(Long userId) {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        return (userId != null ? noteRepository.findAllByCreatedBy_Id(userId) : noteRepository.findAll()).stream()
                .map(noteResponseMapper::map)
                .toList();
    }

    public NoteResponse getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note with id: " + noteId + " not found"));

        return noteResponseMapper.map(note);
    }

    public NoteResponse createNote(CreateNoteRequest createNoteRequest) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with mail: " + email + " not found"));

        Note noteToCreate = createNoteRequestMapper.map(createNoteRequest);
        noteToCreate.setCreatedBy(user);
        Note noteCreated = noteRepository.save(noteToCreate);
        return noteResponseMapper.map(noteCreated);
    }

    public void deleteNoteById(Long noteId) {

        boolean isExist = noteRepository.existsById(noteId);

        if (!isExist) {
            throw new EntityNotFoundException("Note with id: " + noteId + " not found");
        }

        noteRepository.deleteById(noteId);
    }
}
