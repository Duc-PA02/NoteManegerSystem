package com.example.notemanegersystem.service.share;

import com.example.notemanegersystem.dtos.NoteDTO;
import com.example.notemanegersystem.dtos.NoteShareUpdateDTO;
import com.example.notemanegersystem.dtos.RepShareDTO;
import com.example.notemanegersystem.dtos.ShareNoteDTO;
import com.example.notemanegersystem.entity.*;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.exceptions.UnauthorizedException;
import com.example.notemanegersystem.repository.*;
import com.example.notemanegersystem.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShareService implements IShareService{
    private final ShareRepository shareRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final NoteLogRepository noteLogRepository;
    private final ContentRepository contentRepository;
    @Override
    public void shareNoteWithUser(ShareNoteDTO shareNoteDTO) throws DataNotFoundException, UnauthorizedException {
        // Tìm ghi chú cần chia sẻ
        Note note = noteRepository.findById(shareNoteDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("Note not found"));

        // Tìm người gửi (người chia sẻ ghi chú)
        User sender = userRepository.findById(shareNoteDTO.getSenderUserId())
                .orElseThrow(() -> new DataNotFoundException("Sender not found"));

        // Tìm người nhận (người được chia sẻ ghi chú)
        User recipient = userRepository.findById(shareNoteDTO.getRecipientUserId())
                .orElseThrow(() -> new DataNotFoundException("Recipient not found"));

        // Chỉ người tạo mới có quyền chia sẻ ghi chú
        if (!note.getUser().getId().equals(shareNoteDTO.getSenderUserId())){
            throw new UnauthorizedException("Only the creator of the note can share it");
        }

        // Tạo đối tượng Share
        Share share = new Share();
        share.setNote(note);
        share.setUser(sender);
        share.setRecipientUser(recipient);
        share.setIsAccept(true); // Người nhận có thể từ chôi chia sẻ
        shareRepository.save(share);

        // Tạo thông báo cho người nhận
        Notification notification = new Notification();
        notification.setMessage(sender.getFullName() + " đã chia sẻ ghi chú " + note.getTitle() + " cho bạn.");
        notification.setShare(share);
        notificationService.sendNotificationToUser(notification, recipient.getId());

        NoteLog noteLog = NoteLog.builder()
                .note(note)
                .action(sender.getFullName() + " đã chia sẻ ghi chú " + note.getTitle() + " cho " + recipient.getFullName())
                .build();
        noteLogRepository.save(noteLog);
    }

    @Override
    public List<Share> getSharesByRecipient(Integer recipientId) {
        return shareRepository.findByRecipientUserId(recipientId);
    }

    @Override
    public List<Note> getSharedNotes(Integer userId) {
        return shareRepository.findSharedNotesByUserId(userId);
    }

    @Override
    public void repShare(RepShareDTO shareNoteDTO) throws DataNotFoundException, UnauthorizedException {
        Note note = noteRepository.findById(shareNoteDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("Note not found"));

        userRepository.findById(shareNoteDTO.getSenderUserId())
                .orElseThrow(() -> new DataNotFoundException("Sender not found"));

        userRepository.findById(shareNoteDTO.getRecipientUserId())
                .orElseThrow(() -> new DataNotFoundException("Recipient not found"));

        Share share = shareRepository.findById(shareNoteDTO.getShareId())
                .orElseThrow(() -> new DataNotFoundException("share not found"));
        if (!note.getUser().getId().equals(shareNoteDTO.getSenderUserId())){
            throw new UnauthorizedException("Only the creator of the note can share it");
        }

        share.setIsAccept(false);
        shareRepository.save(share);
    }

    @Override
    public Content updateSharedNoteContent(Integer userId, NoteShareUpdateDTO updateNoteDTO) throws DataNotFoundException, AccessDeniedException {
        // Kiểm tra xem người dùng có quyền chỉnh sửa ghi chú này không
        Optional<Share> share = shareRepository.findAcceptedShareByUserIdAndNoteId(userId, updateNoteDTO.getNoteId());
        if (!share.isPresent()) {
            throw new AccessDeniedException("User does not have permission to edit this note");
        }

        // Tìm ghi chú và cập nhật nội dung
        Note note = noteRepository.findById(updateNoteDTO.getNoteId())
                .orElseThrow(() -> new DataNotFoundException("Note not found"));

//        if (!share.get().getRecipientUser().equals(userId)){
//            throw new AccessDeniedException("User does not have permission to edit this note");
//        }
        Content content = Content.builder()
                .note(note)
                .text(updateNoteDTO.getText())
                .build();

        contentRepository.save(content);

        NoteLog noteLog = NoteLog.builder()
                .note(note)
                .action("một người bạn của bạn đã chỉnh sửa nội dung")
                .build();

        noteLogRepository.save(noteLog);
        return content;
    }
}
