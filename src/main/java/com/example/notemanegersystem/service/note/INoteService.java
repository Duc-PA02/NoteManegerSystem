package com.example.notemanegersystem.service.note;

import com.example.notemanegersystem.dtos.NoteContentDTO;
import com.example.notemanegersystem.dtos.NoteDTO;
import com.example.notemanegersystem.dtos.NoteImageDTO;
import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.Note;

public interface INoteService {
    Note createNote(NoteDTO noteDTO) throws Exception;
    Note updateNote(Integer id, NoteDTO noteDTO) throws Exception;
    void deleteNote(Integer id);
    public Image createNoteImage(Integer noteId, NoteImageDTO noteImageDTO) throws Exception;
    public Content createNoteContent(Integer noteId, NoteContentDTO noteContentDTO) throws Exception;
}
