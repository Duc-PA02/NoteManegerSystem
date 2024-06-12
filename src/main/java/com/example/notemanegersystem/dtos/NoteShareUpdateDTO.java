package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteShareUpdateDTO {
    @JsonProperty("note_id")
    private Integer noteId;
    @JsonProperty("text")
    private String text;
}
