package com.senabo.domain.disease.service;

import com.senabo.domain.disease.dto.response.DiseaseResponse;
import com.senabo.domain.disease.entity.Disease;
import com.senabo.domain.disease.repository.DiseaseRepository;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
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
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public DiseaseResponse createDisease(Long id, String diseaseName) {
        Member member = findById(id);
        Disease disease = diseaseRepository.save(
                new Disease(member, diseaseName));
        try {
            diseaseRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return DiseaseResponse.from(disease);
    }

    @Transactional
    public List<Disease> getDisease(Long id) {
        Member member = findById(id);
        List<Disease> diseaseList = diseaseRepository.findByMemberId(member);
        if (diseaseList.isEmpty()) {
            throw new EntityNotFoundException("Disease에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return diseaseList;
    }

    @Transactional
    public List<Disease> getDiseaseWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Disease> diseaseList = diseaseRepository.findDiseaseWeek(member, endTime, startTime);
        if (diseaseList.isEmpty()) {
            throw new EntityNotFoundException("Disease에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return diseaseList;
    }

    @Transactional
    public void removeDisease(Long id) {
        try {
            Member member = findById(id);
            List<Disease> list = diseaseRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public Long getCountLastWeekDiseaseList(Member member, LocalDateTime lastStart) {
        Long count = diseaseRepository.countLastWeekData(member, lastStart);
        return count;
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

}
