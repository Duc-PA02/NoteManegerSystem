package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "label")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "label")
    @JsonManagedReference
    private List<NoteLabel> noteLabels;
}
