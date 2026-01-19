package com.umc.nuvibe.domain.tribe.entity;

import com.umc.nuvibe.domain.tribe.vo.EmojiType;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "emojis",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_emoji_chat_user", columnNames = {"chat_id", "user_id"})
        })
@NoArgsConstructor
public class Emoji extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EmojiType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Emoji(EmojiType type, Chat chat, User user) {
        this.type = type;
        this.chat = chat;
        this.user = user;
    }

    public static Emoji of(EmojiType type, Chat chat, User user) {
        return new Emoji(type, chat, user);
    }



}
