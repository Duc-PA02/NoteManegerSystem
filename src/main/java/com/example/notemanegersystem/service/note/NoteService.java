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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService implements INoteService{
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteLogRepository noteLogRepository;
    private final JwtToken jwtToken;
    private final ContentRepository contentRepository;
    private final ImageRepository imageRepository;
    private final LabelRepository labelRepository;
    private final NoteLabelRepository noteLabelRepository;

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
    public Content createNoteContent(NoteContentDTO noteContentDTO) throws Exception {
        Optional<Note> noteOptional = noteRepository.findById(noteContentDTO.getNoteId());
        if (noteOptional.isPresent()) {
            Note note = noteOptional.get();
            Content content = Content.builder()
                    .text(noteContentDTO.getText())
                    .note(note)
                    .build();
            contentRepository.save(content);
            NoteLog noteLog = NoteLog.builder()
                    .note(note)
                    .action("add content")
                    .build();
            noteLogRepository.save(noteLog);
            return content;
        } else {
            throw new Exception("Note not found with id: " + noteContentDTO.getNoteId());
        }
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

    @Override
    public List<Note> getNotesByUserAndLabel(Integer labelId, Integer userId) throws DataNotFoundException {
        // Kiểm tra xem nhãn có tồn tại không
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new DataNotFoundException("Label not found"));

        // Lấy danh sách các ghi chú của người dùng
        List<Note> userNotes = noteRepository.findNoteByUserId(userId);
        List<Note> filteredNotes = new ArrayList<>();

        // Lọc danh sách các ghi chú có nhãn cụ thể
        for (Note note : userNotes) {
            for (NoteLabel noteLabel : note.getNoteLabels()) {
                if (noteLabel.getLabel().getId().equals(labelId)) {
                    filteredNotes.add(note);
                    break;
                }
            }
        }
        return filteredNotes;
    }

    @Override
    public String noteLabel(NoteLabelDTO noteLabelDTO) throws DataNotFoundException {
        Note note = noteRepository.findByIdAndUserId(noteLabelDTO.getNoteId(), noteLabelDTO.getUserId());
        if (note == null){
            throw new DataNotFoundException("note not found");
        }
        Label label = labelRepository.findById(noteLabelDTO.getLabelId())
                .orElseThrow(() -> new DataNotFoundException("Label not found"));

        NoteLabel noteLabel = new NoteLabel();
        noteLabel.setNote(note);
        noteLabel.setLabel(label);
        noteLabelRepository.save(noteLabel);
        NoteLog noteLog = NoteLog.builder()
                .action("add label " + label.getName())
                .note(note)
                .build();
        noteLogRepository.save(noteLog);
        return "add label successfuly";
    }

    private NoteLog createLog(Note note, String action){
        NoteLog noteLog = new NoteLog();
        noteLog.setNote(note);
        noteLog.setAction(action);
        return noteLog;
    }
}
