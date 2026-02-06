package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.umc.nuvibe.domain.image.entity.Image;

@Entity
@Getter
@Table(
        name = "scraped_images",
        uniqueConstraints = @UniqueConstraint(name = "uk_scraped_images_user_image_tribe", columnNames = {"user_id", "image_id", "tribe_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapedImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scraped_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_scraped_images_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL"))
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tribe_id")
    private Tribe tribe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    private ScrapedImage(User user, Tribe tribe, Image image) {
        this.user = user;
        this.tribe = tribe;
        this.image = image;
    }

    public static ScrapedImage of(User user, Tribe tribe, Image image) {
        return new ScrapedImage(user, tribe, image);
    }
}
