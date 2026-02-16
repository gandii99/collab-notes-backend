package com.example.collabnotes.controller;

import com.example.collabnotes.dto.NoteResponse;
import com.example.collabnotes.service.NoteService;
import com.example.collabnotes.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class NotesWsController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NoteService noteService;


    public NotesWsController(SimpMessagingTemplate messagingTemplate, NoteService noteService) {
        this.messagingTemplate = messagingTemplate;
        this.noteService = noteService;
    }

    @MessageMapping("/note/{noteId}/edit")
    public void edit(@DestinationVariable Long noteId, @Payload NoteEditMessage payload, Principal principal) {
        Long userId = (principal != null) ? Long.parseLong(principal.getName()) : null;
        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }

        NoteResponse savedNote = noteService.updateNoteContentFromWs(noteId, userId, payload.patch());

        NoteUpdateMessage updateMessage = new NoteUpdateMessage(savedNote.getId(), savedNote.getContent(), String.valueOf(userId), System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/note/" + noteId, updateMessage);

    }

    public record NoteEditMessage(String patch) {
    }

    public record NoteUpdateMessage(Long noteId, String patch, String author, Long timestamp) {
    }
}
