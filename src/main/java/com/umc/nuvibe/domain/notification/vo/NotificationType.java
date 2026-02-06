package com.umc.nuvibe.domain.notification.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    NOTI_01("NOTI-01","TRIBE_CHAT_OPENED","{{tag}} 트라이브 챗이 열렸어요. 지금 입장해볼까요?", "채팅", "새로운 트라이브 챗이 열렸어요", "{{tag}} 트라이브 챗에 지금 입장해 보세요!"),
    NOTI_02("NOTI-02","TRIBE_CHAT_JOINED","기다리던 {{tag}} 트라이브 챗이 열렸어요. 지금 들어가볼까요?", "채팅", "기다리던 트라이브 챗이 열렸어요", "{{tag}} 트라이브 챗에 지금 들어가 볼까요?"),
    NOTI_03("NOTI-03","TRIBE_CHAT_IMAGE_UPLOADED","{{tag}} 트라이브 챗에 새로운 바이브가 올라왔어요.", "채팅", "트라이브 챗에 새 바이브가 올라왔어요", "{{tag}} 트라이브 챗을 지금 확인해 보세요!"),
    NOTI_04("NOTI-04","IMAGE_REACTION","{{nickname}}의 바이브에 반응이 남겨졌어요.", "채팅", "사람들이 내 바이브에 반응했어요", "{{tag}} 트라이브 챗 속 반응을 확인해 보세요!"),
    NOTI_05("NOTI-05","TRIBE_CHAT_CLOSING","{{tag}} 트라이브 챗이 하루 뒤 닫혀요.", "알림", "트라이브 챗이 곧 닫혀요", "{{tag}} 트라이브 챗에 마지막으로 참여해 볼까요?"),
    NOTI_06("NOTI-06","TRIBE_CHAT_CLOSED","{{tag}} 트라이브 챗이 종료되었어요.", "알림", "트라이브 챗이 종료되었어요", "또 다른 트라이브를 찾으러 갈까요? "),
    NOTI_07("NOTI-07","MISSION_REMINDER","오늘의 드랍이 아직 비어 있어요. 지금의 무드를 남겨볼까요?", "미션", "오늘의 드랍이 아직 비어 있어요", "지금의 무드를 남겨볼까요?"),
    NOTI_08("NOTI-08","TAG_RECOMMENDATION","오늘의 추천 태그 {{tag}} — 떠오르는 바이브가 있나요?", "미션", "오늘의 추천 태그가 도착했어요", "{{tag}} 태그로 바이브를 드랍해 볼까요?"),
    NOTI_09("NOTI-09","WEEKLY_RECAP_CREATED","이번 주의 바이브 톤이 정리됐어요.", "알림", "이번 주의 바이브 톤이 정리됐어요", "한주의 흐름을 확인해 보세요!"),
    NOTI_10("NOTI-10","FULL_RECAP_UPDATED","지금까지 쌓인 바이브 톤을 한 번에 볼 수 있어요.", "알림", "전체 바이브톤이 정리됐어요", "지금까지 쌓인 바이브를 확인해 볼까요?"),
    NOTI_11("NOTI-11","PASSWORD_CHANGED",null, "알림", "비밀번호가 변경됐어요", "보안을 다시 확인해 볼까요?"),
    NOTI_12("NOTI-12","NICKNAME_CHANGED",null, "알림", "닉네임이 변경됐어요", "14일 후 다시 변경할 수 있어요");

    private final String code;
    private final String clientType;
    private final String pushMessage;
    private final String category;
    private final String mainMessage;
    private final String actionMessage;

    public String formatPushMessage(String tag, String nickname) {
        String formattedTag = (tag != null && !tag.isBlank()) ? "#" + tag + " " : "";
        return pushMessage
                .replace("{{tag}} ", formattedTag)
                .replace("{{tag}}", formattedTag.trim())
                .replace("{{nickname}}", nickname != null ? nickname : "");
    }

    public String formatMainMessage(String tag) {
        String formattedTag = (tag != null && !tag.isBlank()) ? "#" + tag + " " : "";
        return mainMessage
                .replace("{{tag}} ", formattedTag)
                .replace("{{tag}}", formattedTag.trim());
    }

    public String formatActionMessage(String tag) {
        String formattedTag = (tag != null && !tag.isBlank()) ? "#" + tag + " " : "";
        return actionMessage
                .replace("{{tag}} ", formattedTag)
                .replace("{{tag}}", formattedTag.trim());
    }
}
