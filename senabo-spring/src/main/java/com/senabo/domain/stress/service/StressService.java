package com.senabo.domain.stress.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.service.ReportService;
import com.senabo.domain.stress.dto.response.StressResponse;
import com.senabo.domain.stress.entity.Stress;
import com.senabo.domain.stress.entity.StressType;
import com.senabo.domain.stress.repository.StressRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StressService {
    private final StressRepository stressRepository;
    private final MemberService memberService;
    private final ReportService reportService;

    @Transactional
    public StressResponse saveStress(Member member, StressType type, int changeAmount) {
        log.info("스트레스 저장");
        int originStress = member.getStressLevel();
        int score = originStress + changeAmount;
        if (score > 100) score = 100;
        if (score < 0) score = 0;
        Stress stress = stressRepository.save(
                new Stress(member, type, changeAmount, score)
        );
        member.updateStress(score);
        try {
            stressRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return StressResponse.from(stress);
    }


    @Transactional
    public StressResponse createStress(String email, StressType type, int changeAmount) {
        Member member = memberService.findByEmail(email);
        return saveStress(member, type, changeAmount);
    }

    @Transactional
    public List<Stress> getStress(String email) {
        Member member = memberService.findByEmail(email);
        List<Stress> stressList = stressRepository.findByMemberId(member);
        return stressList;
    }

    @Transactional
    public List<Stress> getStressWeek(String email, int week) {
        Member member = memberService.findByEmail(email);
        List<Stress> stressList = null;
        Optional<Report> result = reportService.findReportWeek(member, week);
        if (result.isEmpty()) return stressList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        stressList = stressRepository.findStressWeek(member, endTime, startTime);
        return stressList;
    }

    @Transactional
    public Stress getLatestStressData(Member member) {
        return stressRepository.findLatestData(member);
    }

    @Transactional
    public List<Stress> getLastWeekList(Member member, LocalDateTime lastStart, StressType type) {
        List<Stress> list = stressRepository.findLastWeekData(member, lastStart, type);
        return list;
    }

    @Transactional
    public Long getCountLastWeekList(Member member, LocalDateTime lastStart, StressType type) {
        Long count = stressRepository.countLastWeekData(member, lastStart, type);
        return count;
    }

}
