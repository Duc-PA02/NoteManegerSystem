package com.example.notemanegersystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareNoteDTO {
    private Integer noteId;
    private Integer recipientUserId;
    private Integer senderUserId;
}
