package com.example.notemanegersystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepShareDTO {
    private Integer noteId;
    private Integer shareId;
    private Integer recipientUserId;
    private Integer senderUserId;
    private Boolean isAccept;
}
