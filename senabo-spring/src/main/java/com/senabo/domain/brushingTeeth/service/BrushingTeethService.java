package com.senabo.domain.brushingTeeth.service;

import com.senabo.domain.brushingTeeth.dto.response.BrushingTeethResponse;
import com.senabo.domain.brushingTeeth.entity.BrushingTeeth;
import com.senabo.domain.brushingTeeth.repository.BrushingTeethRepository;
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
public class BrushingTeethService {
    private final BrushingTeethRepository brushingTeethRepository;
    private final ReportService reportService;
    private final MemberService memberService;

    @Transactional
    public BrushingTeethResponse createBrushingTeeth(String email) {
        Member member = memberService.findByEmail(email);
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
    public List<BrushingTeeth> getBrushingTeeth(String email) {
        Member member = memberService.findByEmail(email);
        List<BrushingTeeth> brushingTeethList = brushingTeethRepository.findByMemberId(member);
        return brushingTeethList;
    }
    @Transactional
    public List<BrushingTeeth> getBrushingTeethWeek(String email, int week) {
        List<BrushingTeeth> brushingTeethList = new ArrayList<>();
        Member member = memberService.findByEmail(email);
        Optional<Report> result = reportService.findReportWeek(member, week);
        if(result.isEmpty()) return brushingTeethList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        brushingTeethList = brushingTeethRepository.findBrushingTeethWeek(member, endTime, startTime);
        return brushingTeethList;
    }

    @Transactional
    public void removeBrushingTeeth(String email) {
        try {
            Member member = memberService.findByEmail(email);
            List<BrushingTeeth> list = brushingTeethRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

}
