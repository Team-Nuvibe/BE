package com.umc.nuvibe.domain.tribe.service.internal;

import com.umc.nuvibe.domain.tribe.dto.internal.CloseTargetView;
import com.umc.nuvibe.domain.tribe.repository.TribeRepository;
import com.umc.nuvibe.domain.tribe.vo.UserTribeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TribeCloseRunner {

    private final TribeRepository tribeRepository;
    private final TribeCloseProcessor processor;

    // 활성화 인원이 5명 미만일 시 삭제 대상
    private static final int MIN_ACTIVE_COUNT = 5;
    // 1회 최대 처리 개수
    private static final int MAX_PROCESS_LIMIT = 100;
    // 삭제 대기 시간
    private static final long GRACE_HOURS = 168L;

    public void run(LocalDateTime batchNow) {

        // 1. 배치 시각으로부터 7일 전 시각
        LocalDateTime cutoff = batchNow.minusHours(GRACE_HOURS);

        // 2. 최대 처리 개수에 맞춘 페이지 생성
        Pageable pageable = PageRequest.of(0, MAX_PROCESS_LIMIT);

        // 3. 활성화 인원 5명인 상태가 7일동안 유지된 트라이브들 반환
        Slice<CloseTargetView> closeTargets = tribeRepository.findCloseTargets(
                cutoff,
                UserTribeStatus.ACTIVE,
                MIN_ACTIVE_COUNT,
                pageable
        );

        // 4. 반환된 트라이브들 대상으로 processor 호출해 삭제
        for (CloseTargetView t : closeTargets) {
            try {
                processor.processTribeClose(t.getTribeId());
            } catch (Exception ex) {
                // 실패 시 다음 tribe로 계속 진행
                // 로그 필요할 시 향후 추가
            }
        }
    }


}
