package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "note")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "is_pinned")
    private Boolean isPinned = false;
    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_note_user"))
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "note")
    @JsonManagedReference
    private List<Content> contents;

    @OneToMany(mappedBy = "note")
    @JsonManagedReference
    private List<Image> images;

    @OneToMany(mappedBy = "note")
    @JsonManagedReference
    private List<NoteLabel> noteLabels;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Share> shares;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<NoteLog> noteLogs;
}
