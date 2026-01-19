package com.umc.nuvibe.domain.archive.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class RecapMessage {

    @Getter
    @AllArgsConstructor
    public enum Day {
        MONDAY("한 주의 시작을 기록해요", "주로 한 주의 시작에 기록해요"),
        TUESDAY("주로 화요일에 드랍해요", "화요일에 가장 자주 남겨요"),
        WEDNESDAY("수요일에 자주 기록해요", "수요일에 기록이 가장 많아요"),
        THURSDAY("목요일에 감각이 많아요", "목요일에 감각을 자주 남겨요"),
        FRIDAY("금요일에 가장 활발해요", "금요일에 가장 활발해요"),
        SATURDAY("주말에 집중적으로 남겨요", "주말에 집중해서 기록해요"),
        SUNDAY("여유 있는 날에 기록해요", "여유로운 날에 자주 남겨요");

        private final String weeklyMessage;
        private final String totalMessage;

        public String getMessage(RecapPeriod period) {
            return (period == RecapPeriod.WEEK) ? weeklyMessage : totalMessage;
        }

        public static Day from(String dayName) {
                return Day.valueOf(dayName.toUpperCase());
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Preference {
        DEEP("감각을 깊게 쌓아요", "감각을 차분하게 쌓았어요"),
        EXPLORE("새로운 무드를 시도해요", "다양한 무드를 꾸준히 시도했어요"),
        BALANCE("균형 있게 기록해요", "감각과 탐색의 균형을 유지했어요"),
        ;

        private final String weeklyMessage;
        private final String totalMessage;

        public String getMessage(RecapPeriod period) {
            return (period == RecapPeriod.WEEK) ? weeklyMessage : totalMessage;
        }

        public static Preference calculate(long tagCount, long boardCount) {
            if (tagCount < boardCount) return DEEP;
            if (tagCount > boardCount) return EXPLORE;
            else return BALANCE;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum TimeSlot {
        DAWN("새벽에 방문해요", "주로 새벽에 기록해요"),
        MORNING("하루를 시작하며 남겨요", "드랍으로 하루를 시작해요"),
        AFTERNOON("일상 속에서 기록해요", "일상 중에 자주 기록해요"),
        EVENING("하루를 정리하며 기록해요", "하루를 정리하며 남겨왔어요");

        private final String weeklyMessage;
        private final String totalMessage;

        public String getMessage(RecapPeriod period) {
            return (period == RecapPeriod.WEEK) ? weeklyMessage : totalMessage;
        }

        public static TimeSlot from(int hour) {
            if (hour >= 1 && hour <= 5) return  DAWN;
            else if (hour >= 6 && hour <= 12) return MORNING;
            else if (hour >= 13 && hour <= 18) return AFTERNOON;
            else return EVENING;
        }
    }
}
