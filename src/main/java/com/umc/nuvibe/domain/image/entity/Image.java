package com.umc.nuvibe.domain.image.entity;

import com.umc.nuvibe.domain.image.vo.ImageStatus;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "thumbnail_url", length = 512)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    private ImageTag imageTag;

    @Column(nullable = true, unique = true)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ImageStatus status = ImageStatus.PENDING;

    public void activate() {
        this.status = ImageStatus.ACTIVE;
    }

    public void updateImageUrl(String newUrl) {
        this.imageUrl = newUrl;
    }

    // 썸네일 URL이 없으면 원본 URL을 반환
    public String getThumbnailUrl() {
        return (thumbnailUrl != null && !thumbnailUrl.isBlank())
            ? thumbnailUrl
            : imageUrl;
    }

    public void updateThumbnailUrl(String newUrl) {
        this.thumbnailUrl = newUrl;
    }

}
