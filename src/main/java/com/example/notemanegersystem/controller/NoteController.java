package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.service.CloudinaryService;
import com.example.notemanegersystem.service.note.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final CloudinaryService cloudinaryService;
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
    @DeleteMapping
    public ResponseEntity<?> deleteNote(@RequestBody DeleteNoteDTO deleteNoteDTO) {
        try {
            String msg = noteService.deleteNote(deleteNoteDTO);
            return ResponseEntity.ok().body(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/by-user-label")
    public ResponseEntity<?> getNotesByUserAndLabel(@RequestParam Integer labelId, @RequestParam Integer userId) {
        try {
            List<Note> notes = noteService.getNotesByUserAndLabel(labelId, userId);
            return ResponseEntity.ok(notes);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/note-label")
    public ResponseEntity<?> noteLabel(@RequestBody NoteLabelDTO noteLabelDTO) {
        try {
            String msg = noteService.noteLabel(noteLabelDTO);
            return ResponseEntity.ok().body(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/content")
    public ResponseEntity<?> createNoteContent(@RequestBody NoteContentDTO noteContentDTO) {
        try {
            Content content = noteService.createNoteContent(noteContentDTO);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{noteId}/images")
    public ResponseEntity<?> uploadImage(@PathVariable Integer noteId, @RequestParam("file") MultipartFile file) {
        try {
            Image image = cloudinaryService.uploadAndSaveImage(noteId, file);
            return ResponseEntity.ok(image);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi tải lên hình ảnh: " + e.getMessage());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/pin")
    public ResponseEntity<Note> updateIsPinned(@RequestBody UpdatePinDTO updatePinDTO){
        try {
            Note updatedNote = noteService.updateIsPinned(updatePinDTO);
            return ResponseEntity.ok(updatedNote);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PatchMapping("/archive")
    public ResponseEntity<Note> updateIsArchived(@RequestBody UpdateArchiveDTO updateArchiveDTO){
        try {
            Note updatedNote = noteService.updateIsArchived(updateArchiveDTO);
            return ResponseEntity.ok(updatedNote);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotesByUser(@PathVariable Integer userId) {
        try {
            List<Note> notes = noteService.getNotesByUser(userId);
            return ResponseEntity.ok(notes);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
