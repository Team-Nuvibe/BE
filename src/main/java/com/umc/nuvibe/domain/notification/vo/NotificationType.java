package com.umc.nuvibe.domain.notification.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    NOTI_01("NOTI-01", "#{{tag}} 트라이브 챗이 열렸어요. 지금 입장해볼까요?", "채팅", "#{{tag}} 트라이브 챗이 열렸어요", "지금 입장해보세요"),
    NOTI_02("NOTI-02", "기다리던 #{{tag}} 트라이브 챗이 열렸어요. 지금 들어가볼까요?", "채팅", "기다리던 #{{tag}} 트라이브가 열렸어요", "지금 들어가볼까요?"),
    NOTI_03("NOTI-03", "#{{tag}} 트라이브 챗에 새로운 바이브가 올라왔어요.", "채팅", "#{{tag}} 트라이브에 새로운 바이브가 올라왔어요", "지금 확인해보세요"),
    NOTI_04("NOTI-04", "{{nickname}}의 바이브에 반응이 남겨졌어요.", "채팅", "사람들이 내 바이브에 반응했어요", "반응을 확인해보세요"),
    NOTI_05("NOTI-05", "#{{tag}} 트라이브 챗이 하루 뒤 닫혀요.", "알림", "#{{tag}} 트라이브가 곧 닫혀요", "마지막으로 참여해볼까요?"),
    NOTI_06("NOTI-06", "#{{tag}} 트라이브 챗이 종료되었어요.", "알림", "#{{tag}} 트라이브가 종료되었어요", "아카이브에서 기록을 확인해보세요"),
    NOTI_07("NOTI-07", "오늘의 드랍이 아직 비어 있어요. 지금의 무드를 남겨볼까요?", "미션", "오늘의 드랍이 아직 비어 있어요", "지금의 무드를 남겨볼까요?"),
    NOTI_08("NOTI-08", "오늘의 추천 태그 #{{tag}} — 떠오르는 바이브가 있나요?", "미션", "오늘의 추천 태그 #{{tag}}", "이 태그로 바이브를 드랍해볼까요?"),
    NOTI_09("NOTI-09", "이번 주의 바이브 톤이 정리됐어요.", "알림", "이번 주의 바이브 톤이 정리됐어요", "리캡을 확인해보세요"),
    NOTI_10("NOTI-10", "지금까지 쌓인 바이브 톤을 한 번에 볼 수 있어요.", "알림", "지금까지의 바이브톤이 모였어요", "전체 리캡을 살펴보세요");

    private final String code;             // NOTI-01, NOTI-02, ...
    private final String pushMessage;      // 백그라운드 푸시용
    private final String category;         // UI: 채팅, 알림, 미션
    private final String mainMessage;      // UI: 메인 메시지
    private final String actionMessage;    // UI: 행동 유도 메시지

    public String formatPushMessage(String tag, String nickname) {
        return pushMessage
                .replace("{{tag}}", tag != null ? tag : "")
                .replace("{{nickname}}", nickname != null ? nickname : "");
    }

    public String formatMainMessage(String tag) {
        return mainMessage.replace("{{tag}}", tag != null ? tag : "");
    }
}
