package com.example.notemanegersystem.repository;

import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share, Integer> {
    List<Share> findByRecipientUserId(Integer recipientId);
}
