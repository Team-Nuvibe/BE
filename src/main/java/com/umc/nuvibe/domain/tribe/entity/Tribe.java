package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "tribes",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_tag_name_version",
                    columnNames = {"tag_name", "version"}
            )}
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class Tribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tribe_id")
    private Long id;

    @Column(name = "tag_name")
    @Enumerated(EnumType.STRING)
    private ImageTag imageTag;

    private Integer counts; // 현재 인원

    private Integer version; // 채팅방 버전

    @Enumerated(EnumType.STRING)
    private TribeStatus status;

    // 마지막 채팅 ID (정렬/읽음 기준)
    private Long lastChatId;

    public static Tribe create(ImageTag imageTag, Integer version){
        return Tribe.builder()
                .imageTag(imageTag)
                .counts(0)
                .version(version)
                .status(TribeStatus.INACTIVE)
                .lastChatId(null)
                .build();
    }

    public void incrementCounts() {this.counts++;}

    public boolean isFull() {return this.counts >= 100;}

    public void updateLastChat(Long lastChatId){
        this.lastChatId = lastChatId;
    }

    public void changeStatus(){this.status = TribeStatus.WAITING;}

}
