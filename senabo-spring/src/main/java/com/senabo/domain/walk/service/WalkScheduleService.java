package com.senabo.domain.walk.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkScheduleService {
    private final WalkService walkService;
    private final MemberService memberService;
    // 매일 오전 12시에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleCheckWalk(){
        log.info("산책 스케줄러 실행");
        List<Member> allMember = memberService.findAllMember();
        for(Member member : allMember){
            walkService.scheduleCheckWalk(member);
        }

    }
}
