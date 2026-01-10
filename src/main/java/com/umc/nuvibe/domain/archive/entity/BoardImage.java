package com.umc.nuvibe.domain.archive.entity;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "board_images")
@NoArgsConstructor
public class BoardImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private ArchiveBoard board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @Builder
    public BoardImage(ArchiveBoard board, Image image) {
        this.board = board;
        this.image = image;
    }
}
