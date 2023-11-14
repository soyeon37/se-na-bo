package com.senabo.domain.emergency.service;

import com.senabo.config.firebase.FCMService;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyScheduleService {
    private final EmergencyService emergencyService;
    private final MemberService memberService;
    // 1시간 마다 실행
    @Scheduled(cron = "0 0 0/1 * * *")
    public void scheduleEmergency(){
        log.info("돌발 상황 스케줄러 실행");
        List<Member> allMember = memberService.findAllMember();
        for(Member member : allMember){
            if(member.getDeviceToken() != null){
                emergencyService.ramdomEmergency(member);
            }
        }
    }
}
