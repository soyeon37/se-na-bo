package com.senabo.domain.poop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoopScheduleService {
    private final PoopService poopService;
    // 1시간 마다 실행 ex) 01:00, 02:00, 03:00 ...
//    @Scheduled(cron = "0 0 0/1 * * *")
//    public void schedulePoop(@AuthenticationPrincipal UserDetails principal) {
//        log.info("배변 스케줄러 실행");
//        Long id = 1L;
//        poopService.schedulePoop(principal.getUsername());
//    }
}
