//package com.senabo.domain.feed.service;
//
//import com.senabo.domain.member.entity.Member;
//import com.senabo.domain.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class FeedScheduleService {
//
//    private final MemberService memberService;
//    private final FeedService feedService;
//
//    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void schedulePoop() {
//        log.info("배변 스케줄러 실행");
//        List<Member> allMember = memberService.findAllMember();
//        for(Member member : allMember){
//            feedService.schedulePoop(member);
//        }
//    }
//
//    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void scheduleFeed() {
//        log.info("배식 스케줄러 실행");
//        List<Member> allMember = memberService.findAllMember();
//        for(Member member : allMember){
//            feedService.scheduleFeed(member);
//        }
//    }
//}
