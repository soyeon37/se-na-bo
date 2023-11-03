package com.senabo.domain.stress.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.domain.stress.dto.response.StressResponse;
import com.senabo.domain.stress.entity.Stress;
import com.senabo.domain.stress.entity.StressType;
import com.senabo.domain.stress.repository.StressRepository;
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
public class StressService {
    private final StressRepository stressRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    @Transactional
    public StressResponse saveStress(Member member, StressType type, int changeAmount){
        log.info("스트레스 저장");
        int originStress = member.getStressLevel();
        Stress stress = stressRepository.save(
                new Stress(member, type, changeAmount, originStress + changeAmount)
        );
        member.updateStress(originStress + changeAmount);
        try {
            stressRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return StressResponse.from(stress);
    }


    @Transactional
    public StressResponse createStress(Long id, StressType type, int changeAmount) {
        Member member = findById(id);
        return saveStress(member, type, changeAmount);
    }

    @Transactional
    public List<Stress> getStress(Long id) {
        Member member = findById(id);
        List<Stress> stressList = stressRepository.findByMemberId(member);
        if (stressList.isEmpty()) {
            throw new EntityNotFoundException("Stress에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return stressList;
    }
    @Transactional
    public List<Stress> getStressWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Stress> stressList = stressRepository.findStressWeek(member, endTime, startTime);
        if (stressList.isEmpty()) {
            throw new EntityNotFoundException("Stress에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
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

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
