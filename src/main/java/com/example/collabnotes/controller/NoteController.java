package com.example.collabnotes.controller;

import com.example.collabnotes.dto.CreateNoteRequest;
import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public List<NoteResponse> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{noteId}")
    public NoteResponse getNoteById(@PathVariable Long noteId) {
        return noteService.getNoteById(noteId);
    }

    @PostMapping
    public NoteResponse createNote(@RequestBody CreateNoteRequest createNoteRequest) {
        return noteService.createNote(createNoteRequest);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNoteById(@PathVariable Long noteId) {
        noteService.deleteNoteById(noteId);
    }
}
