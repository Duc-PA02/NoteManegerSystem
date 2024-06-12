package com.example.notemanegersystem.service.share;

import com.example.notemanegersystem.dtos.NoteShareUpdateDTO;
import com.example.notemanegersystem.dtos.RepShareDTO;
import com.example.notemanegersystem.dtos.ShareNoteDTO;
import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.entity.Share;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.exceptions.UnauthorizedException;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface IShareService {
    void shareNoteWithUser(ShareNoteDTO shareNoteDTO) throws DataNotFoundException, UnauthorizedException;
    List<Share> getSharesByRecipient(Integer recipientId);
    List<Note> getSharedNotes(Integer userId);
    void repShare(RepShareDTO shareNoteDTO) throws DataNotFoundException, UnauthorizedException;
    Content updateSharedNoteContent(Integer userId, NoteShareUpdateDTO updateNoteDTO) throws DataNotFoundException, AccessDeniedException;
}
