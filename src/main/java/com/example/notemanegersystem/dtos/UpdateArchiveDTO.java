package com.example.notemanegersystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArchiveDTO {
    private Integer noteId;
    private Boolean isArchived;
    private Integer userId;
}
