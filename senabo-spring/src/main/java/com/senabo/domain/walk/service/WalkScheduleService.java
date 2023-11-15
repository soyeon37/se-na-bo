package com.senabo.domain.walk.service;

import com.senabo.common.message.ParsingMessageService;
import com.senabo.config.firebase.FCMService;
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
    private final ParsingMessageService parsingMessageService;
    private final FCMService fcmService;
    // 매일 오전 12시에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleCheckWalk(){
        log.info("산책 스케줄러 실행");
        List<Member> allMember = memberService.findAllMemberNonComplete();
        for(Member member : allMember){
            walkService.scheduleCheckWalk(member);
            if (member.getDeviceToken() != null) {
                // FCM
                String dogName = parsingMessageService.parseLastCharacter(member.getDogName());
                fcmService.sendNotificationByToken("세상에 나쁜 보호자는 있다", dogName + "가 배변을 했어요!", member.getDeviceToken());
            }
        }
    }

    @Scheduled(cron = "0 0 12/20 * * *")
    public void scheduleSendWalk(){
        log.info("산책 알림 스케줄러 실행");
        List<Member> allMember = memberService.findAllMemberNonComplete();
        for(Member member : allMember){
            if (member.getDeviceToken() != null) {
                // FCM
                String dogName = parsingMessageService.parseLastCharacter(member.getDogName());
                fcmService.sendNotificationByToken("세상에 나쁜 보호자는 있다", dogName + "와 산책은 하셨나요?", member.getDeviceToken());
            }
        }
    }
}
