package com.example.notemanegersystem.repository;

import com.example.notemanegersystem.entity.NoteLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteLabelRepository extends JpaRepository<NoteLabel, Integer> {
}
