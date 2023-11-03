//package com.senabo.domain.member.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ScheduleService {
//    private final ActivityService activityService;
//
//    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void schedulePoop() {
//        // @AuthenticationPrincipal UserDetails userDetails
//        // Member member = findById(principal.getUsername());
//        log.info("배변 스케줄러 실행");
//        Long id = 1L;
//        activityService.schedulePoop(id);
//    }
//
//    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void scheduleFeed() {
//        // @AuthenticationPrincipal UserDetails userDetails
//        // Member member = findById(principal.getUsername());
//        log.info("배식 스케줄러 실행");
//        Long id = 1L;
//        activityService.scheduleFeed(id);
//    }
//
//
//
//    // 매일 오후 12시, 20시에 실행
//    @Scheduled(cron = "0 0 12,20 * * *")
//    public void scheduleWalk() {
//        // @AuthenticationPrincipal UserDetails userDetails
//        // Member member = findById(principal.getUsername());
////        Member member = findById(1L);
//
//        // 산책 푸시 알림
//        /*
//
//         */
//    }
//
//    // 매일 오전 12시 00분에 실행
//    @Scheduled(cron = "0 0 0 * * *")
//    public void scheduleReport(){
//        // @AuthenticationPrincipal UserDetails userDetails
//        // Member member = findById(principal.getUsername());
//        log.info("리포트 스케줄러 실행");
//        Long id = 1L;
//        activityService.scheduleReport(id);
//
//    }
//
//    // 어플 종료 확인
//
//}
