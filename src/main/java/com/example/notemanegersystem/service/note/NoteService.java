package com.example.notemanegersystem.service.note;

import com.example.notemanegersystem.component.JwtToken;
import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.*;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService implements INoteService{
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteLogRepository noteLogRepository;
    private final JwtToken jwtToken;
    private final ContentRepository contentRepository;
    private final ImageRepository imageRepository;

    @Override
    public Note createNote(String token, NoteDTO noteDTO) throws Exception {
        Integer userId = jwtToken.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        Note note = Note.builder()
                .title(noteDTO.getTitle())
                .isPinned(false)
                .isArchived(false)
                .user(user)
                .build();
        noteRepository.save(note);
        NoteLog noteLog = NoteLog.builder()
                .action("create note")
                .note(note)
                .build();
        noteLogRepository.save(noteLog);
        return note;
    }

    @Override
    public Note updateNote(NoteUpdateDTO noteUpdateDTO) throws Exception {
        Note existingNote = noteRepository.findById(noteUpdateDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("note not found"));
        if (!existingNote.getUser().getId().equals(noteUpdateDTO.getUserId())){
            throw new Exception("You do not have permission to update this note");
        }
        List<NoteLog> noteLogs = new ArrayList<>();
        if (!existingNote.getTitle().equals(noteUpdateDTO.getTitle())){
            noteLogs.add(createLog(existingNote, "Title changed from " + existingNote.getTitle() + " to " + noteUpdateDTO.getTitle()));
            existingNote.setTitle(noteUpdateDTO.getTitle());
        }
        if (!existingNote.getSortOrder().equals(noteUpdateDTO.getSortOrder())){
            noteLogs.add(createLog(existingNote, "SortOrder changed from " + existingNote.getSortOrder() + " to " + noteUpdateDTO.getSortOrder()));
            existingNote.setSortOrder(noteUpdateDTO.getSortOrder());
        }
        if (!existingNote.getIsPinned().equals(noteUpdateDTO.getIsPinned())) {
            noteLogs.add(createLog(existingNote, "Pinned status changed from " + existingNote.getIsPinned() + " to " + noteUpdateDTO.getIsPinned()));
            existingNote.setIsPinned(noteUpdateDTO.getIsPinned());
        }
        if (!existingNote.getIsArchived().equals(noteUpdateDTO.getIsArchived())) {
            noteLogs.add(createLog(existingNote, "Archived status changed from " + existingNote.getIsArchived() + " to " + noteUpdateDTO.getIsArchived()));
            existingNote.setIsArchived(noteUpdateDTO.getIsArchived());
        }
        noteRepository.save(existingNote);
        noteLogRepository.saveAll(noteLogs);
        return existingNote;
    }

    @Override
    public String deleteNote(DeleteNoteDTO deleteNoteDTO) throws Exception {
        Note existingNote = noteRepository.findById(deleteNoteDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("note not found"));
        if (!existingNote.getUser().getId().equals(deleteNoteDTO.getUserId())){
            throw new Exception("You do not have permission to delete this note");
        }
        for (Content content : existingNote.getContents()){
            if (content.getNote().getId() == deleteNoteDTO.getNoteId()){
                contentRepository.delete(content);
            }
        }
        for (Image image : existingNote.getImages()){
            if (image.getNote().getId() == deleteNoteDTO.getNoteId()){
                imageRepository.delete(image);
            }
        }
        noteRepository.delete(existingNote);
        NoteLog noteLog = NoteLog.builder()
                .note(existingNote)
                .action("delete note")
                .build();
        noteLogRepository.save(noteLog);
        return "delete successfully note have id = " + deleteNoteDTO.getNoteId();
    }

    @Override
    public Image createNoteImage(Integer noteId, NoteImageDTO noteImageDTO) throws Exception {
        return null;
    }

    @Override
    public Content createNoteContent(Integer noteId, NoteContentDTO noteContentDTO) throws Exception {
        return null;
    }

    @Override
    public List<Note> noteByUser(Integer userId) throws DataNotFoundException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new DataNotFoundException("user not found");
        }
        List<Note> noteList = noteRepository.findNoteByUserId(userId);
        return noteList;
    }

    private NoteLog createLog(Note note, String action){
        NoteLog noteLog = new NoteLog();
        noteLog.setNote(note);
        noteLog.setAction(action);
        return noteLog;
    }
}
