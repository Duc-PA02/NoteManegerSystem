package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    @JsonProperty("note_id")
    private Integer noteId;
    @JsonProperty("image_url")
    private String imageUrl;
}
