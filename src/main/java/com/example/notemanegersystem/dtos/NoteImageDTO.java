package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteImageDTO {
    @JsonProperty("note_id")
    private Integer noteId;
    @JsonProperty("image_url")
    private String imageUrl;
}
