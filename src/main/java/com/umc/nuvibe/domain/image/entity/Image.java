package com.umc.nuvibe.domain.image.entity;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "images")
@NoArgsConstructor
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    private ImageTag imageTag;
}
