package com.example.notemanegersystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteLabelDTO {
    private Integer noteId;
    private Integer labelId;
    private Integer userId;
}
