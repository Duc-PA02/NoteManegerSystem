package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteUpdateDTO {
    @JsonProperty("note_id")
    private Integer noteId;
    private String title;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("is_pinned")
    private Boolean isPinned;
    @JsonProperty("is_archived")
    private Boolean isArchived;
    @JsonProperty("sort_order")
    private Integer sortOrder;
}
