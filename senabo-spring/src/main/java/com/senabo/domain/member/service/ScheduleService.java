package com.senabo.domain.member.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Walk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ActivityService activityService;
    private final MemberService memberService;

    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
    @Scheduled(cron = "0 0 0/1 * * *")
    public void schedulePoop() {
        // @AuthenticationPrincipal UserDetails userDetails
        // Member member = findById(principal.getUsername());
        Long id = 1L;
        activityService.schedulePoop(id);
    }

    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
    @Scheduled(cron = "0 0 0/1 * * *")
//    @Scheduled(fixedDelay = 10000)
    public void scheduleFeed() {
        // @AuthenticationPrincipal UserDetails userDetails
        // Member member = findById(principal.getUsername());
        Long id = 1L;
        activityService.scheduleFeed(id);
    }

    // 매일 오후 11시 59분에 실행
    @Scheduled(cron = "0 0 23 * * *")
//    @Scheduled(fixedDelay = 10000)
    public void scheduleCheckWalk(){
        // @AuthenticationPrincipal UserDetails userDetails
        // Member member = findById(principal.getUsername());
        Long id = 1L;
        activityService.scheduleCheckWalk(id);
    }// 매일 오후 11시 59분에 실행
    @Scheduled(cron = "0 0 23 * * *")
//    @Scheduled(fixedDelay = 10000)
    public void scheduleReport(){
        // @AuthenticationPrincipal UserDetails userDetails
        // Member member = findById(principal.getUsername());
        Long id = 1L;
        activityService.scheduleReport(id);

    }

    // 어플 종료 확인
}
