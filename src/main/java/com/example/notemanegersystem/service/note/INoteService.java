package com.example.notemanegersystem.service.note;

import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.exceptions.DataNotFoundException;

import java.util.List;

public interface INoteService {
    Note createNote(String token, NoteDTO noteDTO) throws Exception;
    String deleteNote(DeleteNoteDTO deleteNoteDTO) throws Exception;
    Content createNoteContent(NoteContentDTO noteContentDTO) throws Exception;
    List<Note> getNotesByUserAndLabel(Integer userId, Integer labelId) throws DataNotFoundException;
    String noteLabel(NoteLabelDTO noteLabelDTO) throws DataNotFoundException;
    Note updateIsPinned(UpdatePinDTO updatePinDTO) throws Exception;
    Note updateIsArchived(UpdateArchiveDTO updateArchiveDTO) throws Exception;
    List<Note> getNotesByUser(Integer userId);
    void updateNoteOrder(UpdateNoteOrderDTO updateOrderDTO) throws DataNotFoundException;
    List<Note> getNoteByTitle(Integer userId, String title);
}
