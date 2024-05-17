package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "confirmemail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmEmail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @Column(name = "is_confirm")
    private Boolean isConfirm;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_confirmEmail_user"))
    @JsonBackReference
    private User user;
}
