package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    @Column(name = "is_read")
    private Boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "share_id", foreignKey = @ForeignKey(name = "fk_notification_share"))
    @JsonBackReference
    private Share share;
}
