package com.example.notemanegersystem.service.note;

import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.exceptions.DataNotFoundException;

import java.util.List;

public interface INoteService {
    Note createNote(String token, NoteDTO noteDTO) throws Exception;
    Note updateNote(NoteUpdateDTO noteUpdateDTO) throws Exception;
    String deleteNote(DeleteNoteDTO deleteNoteDTO) throws Exception;
    public Image createNoteImage(Integer noteId, NoteImageDTO noteImageDTO) throws Exception;
    public Content createNoteContent(Integer noteId, NoteContentDTO noteContentDTO) throws Exception;
    List<Note> noteByUser(Integer userId) throws DataNotFoundException;
}
