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
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

}
