package com.example.notemanegersystem.repository;

import com.example.notemanegersystem.entity.NoteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteLogRepository extends JpaRepository<NoteLog, Integer> {
}
