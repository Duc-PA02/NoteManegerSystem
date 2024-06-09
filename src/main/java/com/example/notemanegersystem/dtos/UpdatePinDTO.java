package com.example.notemanegersystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePinDTO {
    private Integer noteId;
    private Boolean isPinned;
    private Integer userId;
}
