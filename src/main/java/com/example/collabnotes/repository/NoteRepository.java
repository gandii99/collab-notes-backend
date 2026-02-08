package com.example.collabnotes.repository;

import com.example.collabnotes.entity.Note;
import com.example.collabnotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByCreatedBy_Id(Long userId);
}
