package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notelabel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "label_id", foreignKey = @ForeignKey(name = "fk_notelabel_label"))
    @JsonBackReference
    private Label label;

    @ManyToOne
    @JoinColumn(name = "note_id", foreignKey = @ForeignKey(name = "fk_notelabel_note"))
    @JsonBackReference
    private Note note;
}
