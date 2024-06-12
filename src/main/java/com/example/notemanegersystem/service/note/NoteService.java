package com.example.notemanegersystem.service.note;

import com.example.notemanegersystem.component.JwtToken;
import com.example.notemanegersystem.dtos.*;
import com.example.notemanegersystem.entity.*;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.exceptions.UnauthorizedException;
import com.example.notemanegersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
            if (!note.getUser().getId().equals(noteContentDTO.getUserId())){
                throw new UnauthorizedException("user does not have permission to create content");
            }
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
    public List<Note> getNotesByUserAndLabel(Integer labelId, Integer userId) throws DataNotFoundException {
        // Kiểm tra xem nhãn có tồn tại không
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new DataNotFoundException("Label not found"));

        // Lấy danh sách các ghi chú của người dùng
        List<Note> userNotes = noteRepository.findByUserId(userId);
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

    @Override
    public Note updateIsPinned(UpdatePinDTO updatePinDTO) throws Exception {
        Note existingNote = noteRepository.findById(updatePinDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("note not found"));
        if (!existingNote.getUser().getId().equals(updatePinDTO.getUserId())){
            throw new Exception("You do not have permission to delete this note");
        }
        existingNote.setIsPinned(updatePinDTO.getIsPinned());
        existingNote.setUpdatedAt(LocalDateTime.now());
        Note updatedNote = noteRepository.save(existingNote);
        if (updatedNote.getIsPinned() != null && updatedNote.getIsPinned()){
            updatedNote.setSortOrder(null);
        }
        noteRepository.save(updatedNote);
        NoteLog noteLog = NoteLog.builder()
                .note(updatedNote)
                .action("Updated isPinned to: " + updatePinDTO.getIsPinned())
                .build();
        noteLogRepository.save(noteLog);
        return updatedNote;
    }

    @Override
    public Note updateIsArchived(UpdateArchiveDTO updateArchiveDTO) throws Exception {
        Note existingNote = noteRepository.findById(updateArchiveDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("note not found"));
        if (!existingNote.getUser().getId().equals(updateArchiveDTO.getUserId())){
            throw new Exception("You do not have permission to delete this note");
        }
        existingNote.setIsArchived(updateArchiveDTO.getIsArchived());
        existingNote.setUpdatedAt(LocalDateTime.now());
        Note updatedNote = noteRepository.save(existingNote);
        if (updateArchiveDTO.getIsArchived() != null && updateArchiveDTO.getIsArchived()){
            existingNote.setSortOrder(null);
        }
        noteRepository.save(updatedNote);
        NoteLog noteLog = NoteLog.builder()
                .note(updatedNote)
                .action("Updated isPinned to: " + updateArchiveDTO.getIsArchived())
                .build();
        noteLogRepository.save(noteLog);
        return updatedNote;
    }

    @Override
    public List<Note> getNotesByUser(Integer userId) {
        List<Note> notes = noteRepository.findByUserIdAndIsArchivedFalse(userId);

        // Sắp xếp ghi chú
        notes.sort(Comparator.comparing(Note::getIsPinned, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Note::getSortOrder, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Note::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));

        return notes;
    }

    @Override
    public void updateNoteOrder(UpdateNoteOrderDTO updateOrderDTO) throws DataNotFoundException {
        Note noteToUpdate = noteRepository.findById(updateOrderDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("Note not found"));

        if (!noteToUpdate.getUser().getId().equals(updateOrderDTO.getUserId())) {
            throw new DataNotFoundException("Note does not belong to the user");
        }

        // Nhận tất cả các ghi chú cho người dùng chưa được lưu trữ và sắp xếp theo thứ tự sắp xếp
        List<Note> userNotes = noteRepository.findByUserIdAndIsArchivedFalseOrderBySortOrder(updateOrderDTO.getUserId());

        // Điều chỉnh sắp xếpThứ tự của các ghi chú khác
        adjustSortOrder(userNotes, noteToUpdate, updateOrderDTO.getNewOrder());

        // Đặt thứ tự sắp xếp mới để ghi chú được cập nhật
        noteToUpdate.setSortOrder(updateOrderDTO.getNewOrder());
        noteRepository.save(noteToUpdate);
    }

    @Override
    public List<Note> getNoteByTitle(Integer userId, String title) {
        return noteRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title);
    }

    private void adjustSortOrder(List<Note> userNotes, Note updatedNote, Integer newOrder) {
        // Xóa ghi chú cần cập nhật khỏi danh sách và thêm lại ghi chú ở vị trí mới
        userNotes.remove(updatedNote);

        // Chỉ số của danh sách (index) bắt đầu từ 0, trong khi sortOrder thường bắt đầu từ 1.
        // Do đó, khi thêm một ghi chú vào danh sách userNotes theo thứ tự mới (newOrder), ta cần trừ đi 1 để phù hợp với chỉ số của danh sách.
        // Đảm bảo rằng newOrder không vượt quá kích thước của danh sách, nếu vượt thì chỉ định nó là kích thước danh sách.
        int insertIndex = Math.min(newOrder - 1, userNotes.size());

        // Thêm lại ghi chú ở vị trí mới
        userNotes.add(insertIndex, updatedNote);

        // Kiểm tra và ném ngoại lệ nếu ghi chú đã được ghim hoặc đã lưu trữ
        validateNote(updatedNote);

        // Chỉ định lại thứ tự sắp xếp cho tất cả các ghi chú
        int currentOrder = 1;
        for (Note note : userNotes) {
            note.setSortOrder(currentOrder++);
            noteRepository.save(note);
        }
    }
    private void validateNote(Note note) {
        if (note.getIsPinned() || note.getIsArchived()) {
            throw new IllegalStateException("Cannot update a pinned or archived note");
        }
    }

}
