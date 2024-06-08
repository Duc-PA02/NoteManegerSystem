package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.DeleteNoteDTO;
import com.example.notemanegersystem.dtos.NoteDTO;
import com.example.notemanegersystem.dtos.NoteUpdateDTO;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.service.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestHeader("Authorization") String authHeader, @RequestBody NoteDTO noteDTO) {
        try {
            // Extract token from header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            Note note = noteService.createNote(token, noteDTO);
            return ResponseEntity.ok(note);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateNote(@RequestBody NoteUpdateDTO noteUpdateDTO) {
        try {
            Note note = noteService.updateNote(noteUpdateDTO);
            return ResponseEntity.ok().body(note);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteNote(@RequestBody DeleteNoteDTO deleteNoteDTO) {
        try {
            String msg = noteService.deleteNote(deleteNoteDTO);
            return ResponseEntity.ok().body(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> noteByUser(@RequestParam Integer userId) {
        try {
            List<Note> noteList = noteService.noteByUser(userId);
            return ResponseEntity.ok().body(noteList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
