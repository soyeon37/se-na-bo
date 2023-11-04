package com.senabo.domain.walk.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.service.ReportService;
import com.senabo.domain.walk.dto.request.UpdateWalkRequest;
import com.senabo.domain.walk.dto.response.WalkResponse;
import com.senabo.domain.walk.entity.Walk;
import com.senabo.domain.walk.repository.WalkRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final MemberService memberService;
    private final ReportService reportService;

    @Transactional
    public WalkResponse createWalk(String email) {
        Member member = memberService.findByEmail(email);
        LocalDateTime startTime = LocalDateTime.now();
        Walk walk = walkRepository.save(
                new Walk(member, startTime, null, 0.0));
        try {
            walkRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return WalkResponse.from(walk);
    }

    @Transactional
    public List<Walk> getWalk(String email) {
        Member member = memberService.findByEmail(email);
        List<Walk> walkList = walkRepository.findByMemberId(member);
        return walkList;
    }

    @Transactional
    public List<Walk> getWalkWeek(String email, int week) {
        Member member = memberService.findByEmail(email);
        List<Walk> walkList = new ArrayList<>();
        Optional<Report> result = reportService.findReportWeek(member, week);
        if(result.isEmpty()) return walkList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        walkList = walkRepository.findWalkWeek(member, endTime, startTime);
        return walkList;
    }

    @Transactional
    public WalkResponse updateWalk(String email, UpdateWalkRequest request) {
        Member member = memberService.findByEmail(email);
        Walk walk = walkRepository.findLatestData(member);
        if (walk.getEndTime() == null) {
            walk.update(LocalDateTime.now(), request.distnace());
        } else {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_UPDATE_DATA));
        }
        return WalkResponse.from(walk);
    }

    @Transactional
    public void removeWalk(String email) {
        try {
            Member member = memberService.findByEmail(email);
            List<Walk> list = walkRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

//    @Transactional
//    public void scheduleCheckWalk(Long id) {
//        Member member = findById(id);
//        int originAffection = member.getAffection();
//        int originStress = member.getStressLevel();
//        int changeAffectionAmount = 0;
//        int changeStressAmount = 0;
//
//        LocalDateTime startToday = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
//        List<Walk> list = walkRepository.findTodayData(member, startToday);
//        double totalDistance = 0.0;
//        for (int i = 0; i < list.size(); i++) {
//            totalDistance += list.get(i).getDistance();
//        }
//
//        log.info("총 산책 거리: " + totalDistance);
//
//        boolean complete = false;
//
//        if (totalDistance < 5.0) {
//            changeAffectionAmount = -5;
//            changeStressAmount = 10;
//        } else {
//            complete = true;
//            changeAffectionAmount = 5;
//            changeStressAmount = -10;
//        }
//
//
//        // !complete 미완료 + stress 증가 -> not 100
//        // complete 완료 + stress 감소 -> not 0
//        if ((!complete && originStress != 100) || (complete && originStress != 0)) {
//            saveStress(member, StressType.WALK, changeStressAmount);
//        }
//
//        // 재 계산 필요     if(originAffection >= 96) changeAmount = 100 - originAffection;
//        // 미완료 + affection 감소 -> not 0
//        // 완료 + affection 증가 -> not 100
//        if ((!complete && originAffection >= 5) || (complete && originAffection <= 95)) {
//            saveAffection(member, AffectionType.WALK, changeAffectionAmount);
//        }
//    }
}
