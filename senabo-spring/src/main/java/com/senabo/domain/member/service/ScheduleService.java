package com.senabo.domain.member.service;

import com.senabo.domain.feed.service.FeedService;
import com.senabo.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ReportService reportService;
    private final FeedService feedService;

    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void schedulePoop(@AuthenticationPrincipal UserDetails principal) {
//        log.info("배변 스케줄러 실행");
//        Long id = 1L;
//        poopService.schedulePoop(principal.getUsername());
//    }

    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void scheduleFeed(@AuthenticationPrincipal UserDetails principal) {
//        log.info("배식 스케줄러 실행");
//        Long id = 1L;
//        feedService.scheduleFeed(principal.getUsername());
//    }


    // 매일 오후 12시, 20시에 실행
//    @Scheduled(cron = "0 0 12,20 * * *")
    public void scheduleWalk(@AuthenticationPrincipal UserDetails principal) {
        // @AuthenticationPrincipal UserDetails userDetails
        // Member member = findById(principal.getUsername());
//        Member member = findById(1L);

        // 산책 푸시 알림
        /*

         */
    }



    // 어플 종료 확인

}
