package com.umc.nuvibe.domain.notification.entity;

import com.umc.nuvibe.domain.notification.vo.NotificationType;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_notification_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL"))
    private User user;

    // [추가] 알림 타입
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // [추가] UI 알림함용 - 알림 종류 (채팅, 알림, 미션)
    @Column(nullable = false)
    private String category;

    // [추가] UI 알림함용 - 메인 메시지
    @Column(name = "main_message", nullable = false)
    private String mainMessage;

    // [추가] UI 알림함용 - 행동 유도 메시지
    @Column(name = "action_message", nullable = false)
    private String actionMessage;

    // [추가] 연관 ID (트라이브ID, 이미지ID 등)
    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "tribe_id")
    private Long tribeId;

    @Column(name = "is_read")
    private boolean isRead = false;

    // [추가] Builder 패턴
    @Builder
    private Notification(User user, NotificationType type, String category,
                         String mainMessage, String actionMessage, Long relatedId, Long tribeId) {
        this.user = user;
        this.type = type;
        this.category = category;
        this.mainMessage = mainMessage;
        this.actionMessage = actionMessage;
        this.relatedId = relatedId;
        this.tribeId = tribeId;  // 추가
    }

    public void read() {
        this.isRead = true;
    }
}
