package com.senabo.domain.communication.service;

import com.senabo.common.ActivityType;
import com.senabo.domain.affection.service.AffectionService;
import com.senabo.domain.communication.dto.response.CommunicationResponse;
import com.senabo.domain.communication.entity.Communication;
import com.senabo.domain.communication.repository.CommunicationRepository;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunicationService {
    private final CommunicationRepository communicationRepository;
    private final ReportService reportService;
    private final MemberService memberService;
    private final AffectionService affectionService;

    @Transactional
    public CommunicationResponse createCommunication(String email, ActivityType type) {
        Member member = memberService.findByEmail(email);
        int changeAmount = 5;
        Communication communication = communicationRepository.save(
                new Communication(member, type));
        try {
            communicationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        affectionService.saveAffection(member, type, changeAmount);
        return CommunicationResponse.from(communication);
    }


    @Transactional
    public List<Communication> getCommunication(String email) {
        Member member = memberService.findByEmail(email);
        List<Communication> communicationList = communicationRepository.findByMemberId(member);
        return communicationList;
    }

    @Transactional
    public List<Communication> getCommunicationWeek(String email, int week) {
        List<Communication> communicationList = null;
        Member member = memberService.findByEmail(email);
        Optional<Report> result = reportService.findReportWeek(member, week);
        if (result.isEmpty()) return communicationList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        communicationList = communicationRepository.findCommunicationWeek(member, endTime, startTime);
        return communicationList;
    }

    @Transactional
    public void removeCommunication(String email) {
        try {
            Member member = memberService.findByEmail(email);
            List<Communication> list = communicationRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }
}
