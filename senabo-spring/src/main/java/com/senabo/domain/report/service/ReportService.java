package com.senabo.domain.report.service;

import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.affection.service.AffectionService;
import com.senabo.domain.bath.entity.Bath;
import com.senabo.domain.bath.service.BathService;
import com.senabo.domain.brushingTeeth.service.BrushingTeethService;
import com.senabo.domain.communication.service.CommunicationService;
import com.senabo.domain.disease.service.DiseaseService;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.report.dto.response.ReportResponse;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.domain.stress.entity.Stress;
import com.senabo.domain.stress.entity.StressType;
import com.senabo.domain.stress.service.StressService;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.DataException;
import com.senabo.exception.model.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberService memberService;
    private final AffectionService affectionService;
    private final StressService stressService;
    private final DiseaseService diseaseService;
    private final CommunicationService communicationService;
    private final BathService bathService;
    private final BrushingTeethService brushingTeethService;

    @Transactional
    public List<Report> getReport(String email) {
        Member member = memberService.findByEmail(email);
        List<Report> reportList = reportRepository.findCompleteReport(member);
        return reportList;
    }

    @Transactional
    public Optional<Report> getReportWeek(String email, int week) {
        Member member = memberService.findByEmail(email);
        return findReportWeek(member, week);
    }

    @Transactional
    public Optional<Report> findReportWeek(Member member, int week) {
        Optional<Report> report = reportRepository.findCompleteReportWeek(member, week);
        return report;
    }

    public Report findLatestData(Member member) {
        Optional<Report> reportOptional = reportRepository.findLatestData(member);
        if (reportOptional.isEmpty()) {
            throw new DataException(ExceptionMessage.DATA_NOT_FOUND);
        }
        return reportOptional.get();
    }

    // 앱 사용 시간 저장
    @Transactional
    public ReportResponse updateTotalTime(Member member, int totalTime) {
        Report report = findLatestData(member);
        report.updateTotalTime(totalTime);
        return ReportResponse.from(report);
    }

    public boolean check7Days(Member member) {
        // 마지막 리포트의 create datetime 가져오기
        Report lastReport = findLatestData(member);
        LocalDateTime lastCreateTime = lastReport.getCreateTime();
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime lastStart = lastCreateTime.truncatedTo(ChronoUnit.DAYS);

        // 7일이 되었으면 새로운 report 생성
        Duration duration = Duration.between(now, lastStart);
        long days = duration.toDays();
        if (days == 7) return true;
        return false;
    }

    @Transactional
    public void scheduleReport(Member member) {
        try {
            // 마지막 리포트의 create datetime 가져오기
            Report lastReport = findLatestData(member);
            LocalDateTime lastCreateTime = lastReport.getCreateTime();
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
            LocalDateTime lastStart = lastCreateTime.truncatedTo(ChronoUnit.DAYS);

            // 7일이 되었으면 새로운 report 생성
            Duration duration = Duration.between(now, lastStart);
            long days = duration.toDays();
            if (days < 7) return;

            /**
             * Affection
             */
            Affection affection = affectionService.getLatestAffectionData(member);
            int endAffectionScore = affection.getScore();
            /**
             * Stress
             */
            Stress stress = stressService.getLatestStressData(member);
            int endStressScore = stress.getScore();
            /**
             * 배변 score
             * Poop 테이블에서 lastCreateTime 기준보다 큰 data를 가져온다.
             * 배변 패드를 안 치운 횟수를 100에서 감점한다.
             * poopList의 전체 횟수 - 치우지 않은 횟수 / 168 * 100
             */
            Long poopStressCnt = 0L;
            poopStressCnt = stressService.getCountLastWeekList(member, lastStart, StressType.POOP);
            int poopScore = (int) (1 - poopStressCnt / 168) * 100;

            // 산책 score
            Long walkStressCnt = 0L;
            walkStressCnt = stressService.getCountLastWeekList(member, lastStart, StressType.WALK);
            int walkScore = (int) (1 - walkStressCnt / 7) * 100;

            // 질병 score
            Long diseaseStressCnt = 0L;
            diseaseStressCnt = diseaseService.getCountLastWeekDiseaseList(member, lastStart);
            int diseaseScore = (int) (100 - diseaseStressCnt * 10);
            if (diseaseScore <= 0) diseaseScore = 0;

            // 먹이 score
            Long feedStressCnt = 0L;
            feedStressCnt = stressService.getCountLastWeekList(member, lastStart, StressType.FEED);
            int feedScore = (int) (1 - feedStressCnt / 168) * 100;

            // 교감 score // 교감으로 고쳐야 함
            Long communicationCnt = 0L;
            communicationCnt = communicationService.countCommunicationWeek(member, lastStart);
            int communicationScore = (int) (communicationCnt * 1);
            if (communicationScore > 100) communicationScore = 100;

            // lastreport update
            lastReport.update(endAffectionScore, endStressScore, poopScore, walkScore, feedScore, communicationScore, diseaseScore);
            ReportResponse.from(lastReport);

            // 마지막 주차인지 확인
            if (lastReport.getWeek() == 8) {
                member.complete();
                return;
            }

            // 5주 간격 목욕 -> 20
            if (lastReport.getWeek() >= 5 && stressService.getStressType(member, StressType.BATH).isEmpty()) {
                List<Bath> bathList = bathService.getBath(member.getEmail());
                if (bathList.isEmpty()) stressService.saveStress(member, StressType.BATH, 20);
            }

            // 양치 최소 1회 확인 -> 10
            if (brushingTeethService.getBrushingTeethWeek(lastReport, member).isEmpty())
                stressService.saveStress(member, StressType.BRUSHING_TEETH, 10);

            // newreport save
            Report newReport = reportRepository.save(
                    new Report(member, lastReport.getWeek() + 1, endAffectionScore, endStressScore)
            );
            try {
                reportRepository.flush();
            } catch (DataIntegrityViolationException e) {
                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
