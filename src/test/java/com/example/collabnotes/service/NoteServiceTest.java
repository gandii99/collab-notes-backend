package com.example.collabnotes.service;

import com.example.collabnotes.dto.CreateNoteRequest;
import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.entity.Note;
import com.example.collabnotes.entity.User;
import com.example.collabnotes.mapper.CreateNoteRequestMapper;
import com.example.collabnotes.mapper.NoteResponseMapper;
import com.example.collabnotes.repository.NoteRepository;
import com.example.collabnotes.repository.UserRepository;
import com.example.collabnotes.security.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    NoteRepository noteRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    NoteResponseMapper noteResponseMapper;
    @Mock
    CreateNoteRequestMapper createNoteRequestMapper;
    @Mock
    CurrentUserProvider currentUserProvider;

    @InjectMocks
    NoteService noteService;

    @Test
    void getAllNotes_returnsOnlyForCurrentUser() {
        long userId = 10L;
        when(currentUserProvider.requireUserId()).thenReturn(userId);

        Note n1 = Note.builder()
                .id(1L)
                .build();
        Note n2 = Note.builder()
                .id(2L)
                .build();
        when(noteRepository.findAllByCreatedBy_Id(userId)).thenReturn(List.of(n1, n2));

        when(noteResponseMapper.map(n1)).thenReturn(NoteResponse.builder()
                .id(1L)
                .build());
        when(noteResponseMapper.map(n2)).thenReturn(NoteResponse.builder()
                .id(2L)
                .build());

        List<NoteResponse> result = noteService.getAllNotes();

        assertThat(result).extracting(NoteResponse::getId)
                .containsExactly(1L, 2L);
        verify(noteRepository).findAllByCreatedBy_Id(userId);
    }

    @Test
    void getNoteById_returnsOnlyForCurrentUser() {
        long userId = 10L;
        long noteId = 999L;

        when(currentUserProvider.requireUserId()).thenReturn(userId);
        when(noteRepository.findByIdAndCreatedBy_Id(noteId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.getNoteById(noteId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Note with id: " + noteId + " not found");
    }

    @Test
    void createNote_setsCreatedByToCurrentUser() {
        long userId = 10L;
        when(currentUserProvider.requireUserId()).thenReturn(userId);

        User user = User.builder()
                .id(userId)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        CreateNoteRequest req = new CreateNoteRequest();
        req.setTitle("t");
        req.setContent("c");

        Note mapped = Note.builder()
                .title("t")
                .content("c")
                .build();
        when(createNoteRequestMapper.map(req)).thenReturn(mapped);

        Note saved = Note.builder()
                .id(123L)
                .createdBy(user)
                .title("t")
                .content("c")
                .build();
        when(noteRepository.save(any(Note.class))).thenReturn(saved);

        NoteResponse response = NoteResponse.builder()
                .id(123L)
                .build();
        when(noteResponseMapper.map(saved)).thenReturn(response);

        NoteResponse out = noteService.createNote(req);

        assertThat(out.getId()).isEqualTo(123L);

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(captor.capture());
        assertThat(captor.getValue()
                .getCreatedBy()).isSameAs(user);
    }

    @Test
    void deleteNoteById_deletesOnlyIfOwned_elseThrowsNotFound() {
        long userId = 10L;
        long noteId = 5L;
        when(currentUserProvider.requireUserId()).thenReturn(userId);

        when(noteRepository.deleteByIdAndCreatedBy_Id(noteId, userId)).thenReturn(0L);

        assertThatThrownBy(() -> noteService.deleteNoteById(noteId))
                .isInstanceOf(EntityNotFoundException.class);
    }

}
