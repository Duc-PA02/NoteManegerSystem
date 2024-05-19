package com.example.notemanegersystem.service.note;

import com.example.notemanegersystem.dtos.NoteContentDTO;
import com.example.notemanegersystem.dtos.NoteDTO;
import com.example.notemanegersystem.dtos.NoteImageDTO;
import com.example.notemanegersystem.entity.Content;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService implements INoteService{
    private final NoteRepository noteRepository;

    @Override
    public Note createNote(NoteDTO noteDTO) throws Exception {
        return null;
    }

    @Override
    public Note updateNote(Integer id, NoteDTO noteDTO) throws Exception {
        return null;
    }

    @Override
    public void deleteNote(Integer id) {

    }

    @Override
    public Image createNoteImage(Integer noteId, NoteImageDTO noteImageDTO) throws Exception {
        return null;
    }

    @Override
    public Content createNoteContent(Integer noteId, NoteContentDTO noteContentDTO) throws Exception {
        return null;
    }
}
