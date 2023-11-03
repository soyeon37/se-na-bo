package com.senabo.domain.affection.service;


import com.senabo.domain.affection.dto.response.AffectionResponse;
import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.affection.entity.AffectionType;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.affection.repository.AffectionRepository;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.repository.ReportRepository;
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
public class AffectionService {
    private final AffectionRepository affectionRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public AffectionResponse saveAffection(Member member, AffectionType type, int changeAmount) {
        log.info("애정 저장");
        int originAffection = member.getAffection();
        Affection affection = affectionRepository.save(
                new Affection(member, type, changeAmount, originAffection+changeAmount)
        );
        member.updateAffection(originAffection+changeAmount);
        try {
            affectionRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return AffectionResponse.from(affection);
    }

    public AffectionResponse createAffection(Long id, AffectionType type, int changeAmount) {
        Member member = findById(id);
        return saveAffection(member, type, changeAmount);
    }

    @Transactional
    public List<Affection> getAffection(Long id) {
        Member member = findById(id);
        List<Affection> affectionList = affectionRepository.findByMemberId(member);
        if (affectionList.isEmpty()) {
            throw new EntityNotFoundException("Affection에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return affectionList;
    }

    @Transactional
    public List<Affection> getAffectionWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Affection> affectionList = affectionRepository.findAffectionWeek(member, endTime, startTime);
        if (affectionList.isEmpty()) {
            throw new EntityNotFoundException("Affection에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return affectionList;
    }

    @Transactional
    public Affection getLatestAffectionData(Member member) {
        return affectionRepository.findLatestData(member);
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
