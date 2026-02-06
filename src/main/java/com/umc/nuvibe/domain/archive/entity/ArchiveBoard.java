package com.umc.nuvibe.domain.archive.entity;

import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "archive_board")
@NoArgsConstructor
public class ArchiveBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_archive_board_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL"))
    private User user;

    @Column(length = 15 , nullable = false)
    private String name;

    @Builder
    public ArchiveBoard(User user, String name){
        this.user = user;
        this.name = name;
    }

    public void updateName(String name){
        this.name = name;
    }
}
