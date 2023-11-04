package com.senabo.domain.affection.service;


import com.senabo.common.ActivityType;
import com.senabo.domain.affection.dto.response.AffectionResponse;
import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.affection.repository.AffectionRepository;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.service.ReportService;
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
public class AffectionService {
    private final AffectionRepository affectionRepository;
    private final MemberService memberService;
    private final ReportService reportService;

    @Transactional
    public AffectionResponse saveAffection(Member member, ActivityType type, int changeAmount) {
        int originAffection = member.getAffection();
        int score = originAffection + changeAmount;
        if (score > 100) {
            score = 100;
        } else if (score < 0) {
            score = 0;
        }

        Affection affection = affectionRepository.save(
                new Affection(member, type, changeAmount, score)
        );
        member.updateAffection(score);
        try {
            affectionRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return AffectionResponse.from(affection);
    }

    public AffectionResponse createAffection(String email, ActivityType type, int changeAmount) {
        Member member = memberService.findByEmail(email);
        return saveAffection(member, type, changeAmount);
    }

    @Transactional
    public List<Affection> getAffection(String email) {
        Member member = memberService.findByEmail(email);
        List<Affection> affectionList = affectionRepository.findByMemberId(member);
        return affectionList;
    }

    @Transactional
    public List<Affection> getAffectionWeek(String email, int week) {
        List<Affection> affectionList = new ArrayList<>();
        Member member = memberService.findByEmail(email);
        Optional<Report> result = reportService.findReportWeek(member, week);
        if (result.isEmpty()) return affectionList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        affectionList = affectionRepository.findAffectionWeek(member, endTime, startTime);
        return affectionList;
    }

    @Transactional
    public Affection getLatestAffectionData(Member member) {
        return affectionRepository.findLatestData(member);
    }

}
