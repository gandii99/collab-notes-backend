package com.example.collabnotes.repository;


import com.example.collabnotes.entity.NoteShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteShareRepository extends JpaRepository<NoteShare, Long> {

    boolean existsByNote_IdAndUser_Id(Long noteId, Long userId);

    boolean existsByNote_IdAndUser_IdAndPermission(Long noteId, Long userId, NoteShare.Permission permission);
}
