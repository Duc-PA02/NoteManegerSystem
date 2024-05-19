package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteContentDTO {
    @JsonProperty("note_id")
    private Integer noteId;
    @JsonProperty("text")
    private String text;
}
