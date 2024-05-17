package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notelog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteLog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String action;

    @ManyToOne
    @JoinColumn(name = "note_id", foreignKey = @ForeignKey(name = "fk_noteLog_note"))
    @JsonBackReference
    private Note note;
}
