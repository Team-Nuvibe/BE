package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.tribe.vo.TribeStatus;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tribes")
@NoArgsConstructor
public class Tribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tribe_id")
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    private Integer counts; // 현재 인원

    private Integer version; // 채팅방 버전

    @Enumerated(EnumType.STRING)
    private TribeStatus status;

    public Tribe(String tagName, Integer counts, Integer version, TribeStatus status) {
        this.tagName = tagName;
        this.counts = counts;
        this.version = version;
        this.status = status;
    }

    public void activate(){
        this.status = TribeStatus.ACTIVE;
    }

}
