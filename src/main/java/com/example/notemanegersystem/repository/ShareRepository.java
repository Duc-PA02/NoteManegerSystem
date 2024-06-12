package com.example.notemanegersystem.repository;

import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.entity.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRepository extends JpaRepository<Share, Integer> {
    List<Share> findByRecipientUserId(Integer recipientId);
    @Query("SELECT s.note FROM Share s WHERE s.recipientUser.id = :userId AND s.isAccept = true")
    List<Note> findSharedNotesByUserId(@Param("userId") Integer userId);
    @Query("SELECT s FROM Share s WHERE s.recipientUser.id = :sharedUserId AND s.note.id = :noteId AND s.isAccept = true")
    Optional<Share> findAcceptedShareByUserIdAndNoteId(@Param("sharedUserId") Integer sharedUserId, @Param("noteId") Integer noteId);
}
