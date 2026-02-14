package com.example.collabnotes.repository;

import com.example.collabnotes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByCreatedBy_Id(Long userId);

    Optional<Note> findByIdAndCreatedBy_Id(Long id, Long createdById);
    
    long deleteByIdAndCreatedBy_Id(Long noteId, Long userId);
}
