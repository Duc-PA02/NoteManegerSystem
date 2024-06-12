package com.example.notemanegersystem.service.share;

import com.example.notemanegersystem.dtos.ShareNoteDTO;
import com.example.notemanegersystem.entity.Share;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.exceptions.UnauthorizedException;

import java.util.List;

public interface IShareService {
    void shareNoteWithUser(ShareNoteDTO shareNoteDTO) throws DataNotFoundException, UnauthorizedException;
    List<Share> getSharesByRecipient(Integer recipientId);
}
