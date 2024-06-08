package com.example.notemanegersystem.dtos;

import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.NoteLabel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class NoteDTO {
    private String title;
}
