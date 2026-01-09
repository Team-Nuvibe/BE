package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "emojis")
@NoArgsConstructor
public class Emoji extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EmojiType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;



}
