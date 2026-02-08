package com.example.collabnotes.controller;

import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.entity.Note;
import com.example.collabnotes.service.NoteService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public List<NoteResponse> getAllNotes(@RequestParam(required = false) Long userId) {
        return noteService.getAllNotes(userId);
    }

    @GetMapping("/{noteId}")
    public NoteResponse getNoteById(@PathVariable Long noteId) {
        return noteService.getNoteById(noteId);
    }

    @PostMapping
    public NoteResponse createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNoteById(@PathVariable Long noteId) {
        noteService.deleteNoteById(noteId);
    }
}
