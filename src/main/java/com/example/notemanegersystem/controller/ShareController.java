package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.ShareNoteDTO;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.entity.Share;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.exceptions.UnauthorizedException;
import com.example.notemanegersystem.service.share.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shares")
@RequiredArgsConstructor
public class ShareController {
    private final ShareService shareService;
    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<Share>> getSharesByRecipient(@PathVariable Integer recipientId) {
        try {
            List<Share> shares = shareService.getSharesByRecipient(recipientId);
            return ResponseEntity.ok(shares);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping()
    public ResponseEntity<?> shareNote(@RequestBody ShareNoteDTO shareNoteDTO){
        try {
            shareService.shareNoteWithUser(shareNoteDTO);
            return ResponseEntity.ok().body("Note shared successfully");
        }catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UnauthorizedException e) {
            throw new RuntimeException(e);
        }
    }
}
