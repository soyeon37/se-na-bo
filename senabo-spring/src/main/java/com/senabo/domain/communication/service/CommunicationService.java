package com.senabo.domain.communication.service;

import com.senabo.domain.communication.dto.response.CommunicationResponse;
import com.senabo.domain.communication.entity.Communication;
import com.senabo.domain.communication.repository.CommunicationRepository;
import com.senabo.domain.communication.entity.ActivityType;
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
public class CommunicationService {
    private final CommunicationRepository communicationRepository;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommunicationResponse createCommunication(Long id, ActivityType type) {
        Member member = findById(id);
        Communication communication = communicationRepository.save(
                new Communication(member, type));
        try {
            communicationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return CommunicationResponse.from(communication);
    }



    @Transactional
    public List<Communication> getCommunication(Long id) {
        Member member = findById(id);
        List<Communication> communicationList = communicationRepository.findByMemberId(member);
        if (communicationList.isEmpty()) {
            throw new EntityNotFoundException("Communication에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return communicationList;
    }
    @Transactional
    public List<Communication> getCommunicationWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Communication> communicationList = communicationRepository.findCommunicationWeek(member, endTime, startTime);
        if (communicationList.isEmpty()) {
            throw new EntityNotFoundException("Communication에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return communicationList;
    }

    @Transactional
    public void removeCommunication(Long id) {
        try {
            Member member = findById(id);
            List<Communication> list = communicationRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }


    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
