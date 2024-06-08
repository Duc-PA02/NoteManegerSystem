package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteNoteDTO {
    @JsonProperty("note_id")
    private Integer noteId;
    @JsonProperty("user_id")
    private Integer userId;
}
