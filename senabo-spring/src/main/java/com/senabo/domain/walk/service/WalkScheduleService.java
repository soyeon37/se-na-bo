package com.senabo.domain.walk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkScheduleService {
    private final WalkService walkService;
    // 매일 오후 11시 59분에 실행
//    @Scheduled(cron = "0 0 23 * * *")
//    public void scheduleCheckWalk(){
//        // @AuthenticationPrincipal UserDetails userDetails
//        // Member member = findById(principal.getUsername());
//        log.info("산책 스케줄러 실행");
//        Long id = 1L;
//        walkService.scheduleCheckWalk(id);
//    }
}
