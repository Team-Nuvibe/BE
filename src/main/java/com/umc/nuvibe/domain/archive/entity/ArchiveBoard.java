package com.umc.nuvibe.domain.archive.entity;

import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "archive_board")
@NoArgsConstructor
public class ArchiveBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20) //길이 제한 추가
    private String name;

    //빌더 검증 로직 추가
    @Builder
    public ArchiveBoard(User user, String name){
        if (user == null) {
        throw new IllegalArgumentException("사용자는 필수입니다.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("보드 이름은 필수입니다.");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("보드 이름은 20자를 초과할 수 없습니다.");
        }
        this.user = user;
        this.name = name.trim();
    }
    // 검증 로직 추가
    // 보드 이름 수정
    public void updateName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("보드 이름은 필수입니다.");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("보드 이름은 20자를 초과할 수 없습니다.");
        }
        
        this.name = name.trim();
    }


}
