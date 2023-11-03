package com.senabo.domain.brushingTeeth.service;

import com.senabo.domain.brushingTeeth.dto.response.BrushingTeethResponse;
import com.senabo.domain.brushingTeeth.entity.BrushingTeeth;
import com.senabo.domain.brushingTeeth.repository.BrushingTeethRepository;
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
public class BrushingTeethService {
    private final BrushingTeethRepository brushingTeethRepository;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BrushingTeethResponse createBrushingTeeth(Long id) {
        Member member = findById(id);
        BrushingTeeth brushingTeeth = brushingTeethRepository.save(
                new BrushingTeeth(member));
        try {
            brushingTeethRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return BrushingTeethResponse.from(brushingTeeth);
    }

    @Transactional
    public List<BrushingTeeth> getBrushingTeeth(Long id) {
        Member member = findById(id);

        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findByMemberId(member);
        if (brushingTeethList.isEmpty()) {
            throw new EntityNotFoundException("Brushing Teeth에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return brushingTeethList;
    }
    @Transactional
    public List<BrushingTeeth> getBrushingTeethWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findBrushingTeethWeek(member, endTime, startTime);
        if (brushingTeethList.isEmpty()) {
            throw new EntityNotFoundException("BrushingTeeth에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return brushingTeethList;
    }

    @Transactional
    public void removeBrushingTeeth(Long id) {
        try {
            Member member = findById(id);
            List<BrushingTeeth> list = brushingTeethRepository.deleteByMemberId(member);

        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }


    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

}
