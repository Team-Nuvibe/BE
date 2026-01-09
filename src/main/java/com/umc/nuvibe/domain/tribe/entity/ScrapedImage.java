package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import com.umc.nuvibe.domain.image.entity.Image;

@Entity
@Getter
@Table(name = "scraped_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tribe_id")
    private Tribe tribe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;
}
