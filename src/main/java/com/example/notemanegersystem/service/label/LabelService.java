package com.example.notemanegersystem.service.label;

import com.example.notemanegersystem.dtos.LabelDTO;
import com.example.notemanegersystem.entity.Label;
import com.example.notemanegersystem.entity.Note;
import com.example.notemanegersystem.entity.NoteLabel;
import com.example.notemanegersystem.exceptions.DataNotFoundException;
import com.example.notemanegersystem.repository.LabelRepository;
import com.example.notemanegersystem.repository.NoteLabelRepository;
import com.example.notemanegersystem.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService implements ILabelService{
    private final LabelRepository labelRepository;
    private final NoteLabelRepository noteLabelRepository;
    private final NoteRepository noteRepository;
    @Override
    @Transactional
    public String createLabel(LabelDTO labelDTO) {
        Label label = Label.builder()
                .name(labelDTO.getName())
                .build();
        labelRepository.save(label);
        return "create successfully";
    }

    @Override
    public String deleteLabel(Integer id) throws DataNotFoundException {
        Label label = labelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("label notfound"));
        for (NoteLabel noteLabel : label.getNoteLabels()){
            if (noteLabel.getLabel().getId() == id){
                noteLabelRepository.delete(noteLabel);
            }
        }
        labelRepository.delete(label);
        return "delete successfully label have id = " + id;
    }

    @Override
    public List<Label> labelByNote(Integer noteId) throws DataNotFoundException {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new DataNotFoundException("note notFound"));
        List<Label> labels = new ArrayList<>();
        for (NoteLabel noteLabel : note.getNoteLabels()){
            if (noteLabel.getNote().getId() == noteId){
                labels.add(noteLabel.getLabel());
            }
        }
        return labels;
    }
}
