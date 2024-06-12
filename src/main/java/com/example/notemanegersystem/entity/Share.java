package com.example.notemanegersystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "share")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Share extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "is_accept")
    private Boolean isAccept;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_share_user"))
    @JsonBackReference
    private User user; // Người gửi chia sẻ

    @ManyToOne
    @JoinColumn(name = "recipient_user_id", foreignKey = @ForeignKey(name = "fk_share_recipient_user"))
    @JsonBackReference
    private User recipientUser; // Người nhận chia sẻ

    @ManyToOne
    @JoinColumn(name = "note_id", foreignKey = @ForeignKey(name = "fk_share_note"))
    @JsonBackReference
    private Note note;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Notification> notifications;
}
