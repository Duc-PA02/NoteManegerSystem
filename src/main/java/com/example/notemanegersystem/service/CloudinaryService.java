package com.example.notemanegersystem.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.notemanegersystem.entity.Image;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.entity.NoteLog;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.ImageRepository;
import com.example.notemanegersystem.repository.NoteLogRepository;
import com.example.notemanegersystem.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private NoteLogRepository noteLogRepository;

    public Image uploadAndSaveImage(Integer noteId, MultipartFile file) throws IOException, DataNotFoundException {
        // Tải lên hình ảnh lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = uploadResult.get("secure_url").toString();

        // Lấy Note từ cơ sở dữ liệu
        Note existingNote = noteRepository.findById(noteId).orElse(null);
        if (existingNote == null) {
            throw new DataNotFoundException("Note không tồn tại");
        }

        // Lưu thông tin hình ảnh vào cơ sở dữ liệu
        Image image = new Image();
        image.setNote(existingNote);
        image.setImageUrl(imageUrl);
        imageRepository.save(image);

        // Lưu log
        NoteLog noteLog = NoteLog.builder()
                .note(existingNote)
                .action("Thêm hình ảnh: " + imageUrl)
                .build();
        noteLogRepository.save(noteLog);

        return image;
    }
}
