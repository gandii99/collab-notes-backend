package com.example.collabnotes.service;

import com.example.collabnotes.dto.CreateNoteRequest;
import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.dto.UpdateNoteRequest;
import com.example.collabnotes.entity.Note;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.CreateNoteRequestMapper;
import com.example.collabnotes.mapper.NoteResponseMapper;
import com.example.collabnotes.repository.NoteRepository;
import com.example.collabnotes.repository.UserRepository;
import com.example.collabnotes.security.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteResponseMapper noteResponseMapper;
    private final CreateNoteRequestMapper createNoteRequestMapper;
    private final CurrentUserProvider currentUserProvider;

    public List<NoteResponse> getAllNotes() {

        Long userId = currentUserProvider.requireUserId();

        return noteRepository.findAllByCreatedBy_Id(userId)
                .stream()
                .map(noteResponseMapper::map)
                .toList();
    }

    public NoteResponse getNoteById(Long noteId) {
        Long userId = currentUserProvider.requireUserId();

        Note note = noteRepository.findByIdAndCreatedBy_Id(noteId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Note with id: " + noteId + " not found"));

        return noteResponseMapper.map(note);
    }

    public NoteResponse createNote(CreateNoteRequest createNoteRequest) {
        Long userId = currentUserProvider.requireUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Note noteToCreate = createNoteRequestMapper.map(createNoteRequest);
        noteToCreate.setCreatedBy(user);
        Note noteCreated = noteRepository.save(noteToCreate);
        return noteResponseMapper.map(noteCreated);
    }

    @Transactional
    public NoteResponse updateNote(Long noteId, UpdateNoteRequest updateNoteRequest) {

        if (updateNoteRequest == null || (updateNoteRequest.getTitle() == null && updateNoteRequest.getContent() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PATCH body must contain at least one field: title or content");
        }

        Long userId = currentUserProvider.requireUserId();

        Note note = noteRepository.findByIdAndCreatedBy_Id(noteId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Note with id: " + noteId + " not found"));

        if (updateNoteRequest.getTitle() != null) {
            note.setTitle(updateNoteRequest.getTitle());
        }
        if (updateNoteRequest.getContent() != null) {
            note.setContent(updateNoteRequest.getContent());
        }

        Note savedNote = noteRepository.save(note);

        return noteResponseMapper.map(savedNote);
    }

    @Transactional
    public void deleteNoteById(Long noteId) {
        Long userId = currentUserProvider.requireUserId();


        long deletedNotes = noteRepository.deleteByIdAndCreatedBy_Id(noteId, userId);

        if (deletedNotes == 0) {
            throw new EntityNotFoundException("Note with id: " + noteId + " not found");
        }
    }
}
