package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.dtos.LabelDTO;
import com.example.notemanegersystem.entity.Label;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.service.label.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("label")
public class LabelController {
    private final LabelService labelService;
    @PostMapping
    public String createLabel(@RequestBody LabelDTO labelDTO){
        return labelService.createLabel(labelDTO);
    }
    @DeleteMapping
    public String deleteLabel(@RequestParam Integer id) throws DataNotFoundException {
        return labelService.deleteLabel(id);
    }
    @GetMapping("/labelByNote")
    public ResponseEntity<?> labelByNote(@RequestParam Integer noteId) throws DataNotFoundException {
        try {
            List<Label> labels = labelService.labelByNote(noteId);
            return ResponseEntity.ok().body(labels);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
