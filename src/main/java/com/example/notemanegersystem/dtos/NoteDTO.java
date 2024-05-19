package com.example.notemanegersystem.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteDTO {
    private String title;
    @JsonProperty("sort_oder")
    private Integer sortOrder;
    @JsonProperty("user_id")
    private Integer userId;
}
