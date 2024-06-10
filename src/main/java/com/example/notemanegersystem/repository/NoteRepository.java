package com.example.notemanegersystem.repository;

import com.example.notemanegersystem.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByUserId(Integer userId);
    Note findByIdAndUserId(Integer noteId, Integer userId);
    List<Note> findByUserIdAndIsArchivedFalse(Integer userId);
    List<Note> findByUserIdAndIsArchivedFalseOrderBySortOrder(Integer userId);
}
