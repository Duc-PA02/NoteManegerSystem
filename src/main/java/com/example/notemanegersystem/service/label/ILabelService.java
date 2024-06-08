package com.example.notemanegersystem.service.label;

import com.example.notemanegersystem.dtos.LabelDTO;
import com.example.notemanegersystem.entity.Label;
import com.example.notemanegersystem.exceptions.DataNotFoundException;

import java.util.List;

public interface ILabelService {
    String createLabel(String token, LabelDTO labelDTO);
    String deleteLabel(Integer id) throws DataNotFoundException;
    List<Label> labelByNote(Integer noteId) throws DataNotFoundException;
}
