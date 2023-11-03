package com.senabo.domain.walk.service;

import com.senabo.domain.affection.entity.AffectionType;
import com.senabo.domain.stress.entity.StressType;
import com.senabo.domain.walk.dto.request.UpdateWalkRequest;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.domain.walk.dto.request.CreateWalkRequest;
import com.senabo.domain.walk.dto.response.WalkResponse;
import com.senabo.domain.walk.entity.Walk;
import com.senabo.domain.walk.repository.WalkRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalkService {
    private final WalkRepository walkRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public WalkResponse createWalk(Long id, CreateWalkRequest request) {
        Member member = findById(id);
        Walk walk = walkRepository.save(
                new Walk(member, request.startTime(), null, 0.0));
        try {
            walkRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return WalkResponse.from(walk);
    }

    @Transactional
    public List<Walk> getWalk(Long id) {
        Member member = findById(id);
        List<Walk> walkList = walkRepository.findByMemberId(member);
        if (walkList.isEmpty()) {
            throw new EntityNotFoundException("Walk에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return walkList;
    }

    @Transactional
    public List<Walk> getWalkWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Walk> walkList = walkRepository.findWalkWeek(member, endTime, startTime);
        if (walkList.isEmpty()) {
            throw new EntityNotFoundException("Walk에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return walkList;
    }

    @Transactional
    public WalkResponse updateWalk(Long id, UpdateWalkRequest request) {
        Member member = findById(id);
        Walk walk = walkRepository.findLatestData(member);
        if (walk.getEndTime() == null) {
            walk.update(request.endTime(), request.distnace());
        } else {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_UPDATE_DATA));
        }
        return WalkResponse.from(walk);
    }

    @Transactional
    public void removeWalk(Long id) {
        try {
            Member member = findById(id);
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
//
//        // 미완료 + affection 감소 -> not 0
//        // 완료 + affection 증가 -> not 100
//        if ((!complete && originAffection != 0) || (complete && originAffection != 100)) {
//            saveAffection(member, AffectionType.WALK, changeAffectionAmount);
//        }
//    }



    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
