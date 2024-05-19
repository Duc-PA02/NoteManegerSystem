package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmemail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmEmail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @Column(name = "is_confirm")
    private Boolean isConfirm;
    @Column(name = "requiredtime")
    private LocalDateTime requiredTime;
    @Column(name = "expiredtime")
    private LocalDateTime expiredTime;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_confirmEmail_user"))
    @JsonBackReference
    private User user;
}
