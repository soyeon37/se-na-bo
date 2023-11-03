package com.senabo.domain.report.service;

import com.senabo.domain.report.dto.request.UpdateTotalTimeRequest;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.dto.response.ReportResponse;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<Report> getReport(Long id) {
        Member member = findById(id);
        List<Report> reportList = reportRepository.findByMemberId(member);
        if(reportList.isEmpty()){
            throw new EntityNotFoundException("Report에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return reportList;
    }

    @Transactional
    public List<Report> getReportWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Report> reportList = reportRepository.findReportWeek(member, endTime, startTime);
        if(reportList.isEmpty()){
            throw new EntityNotFoundException("Report에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return reportList;
    }

    // 앱 사용 시간 저장
    @Transactional
    public ReportResponse updateTotalTime(Long id, UpdateTotalTimeRequest request){
        Member member = findById(id);
        Duration duration = Duration.between(request.startTime(), request.endTime());
        int hour = (int) duration.toHours();
        Report report = reportRepository.findLatestData(member);
        report.updateTotalTime(hour);
        return  ReportResponse.from(report);
    }

    //    @Transactional
//    public void scheduleReport(Long id) {
//        // 마지막 리포트의 create datetime 가져오기
//        Member member = findById(id);
//        Report lastReport = reportRepository.findLatestData(member);
//        LocalDateTime lastCreateTime = lastReport.getCreateTime();
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime lastStart = lastCreateTime.truncatedTo(ChronoUnit.DAYS);
//        // 7일이 되었으면 새로운 report 생성
//        Duration duration = Duration.between(now, lastCreateTime);
//        long days = duration.toDays();
//        if (days >= 7 && !lastReport.getComplete()) {
//            /**
//             * Affection
//             */
//            Affection affection = getLatestAffectionData(member);
//            int endAffectionScore = affection.getScore();
//            /**
//             * Stress
//             */
//            Stress stress = getLatestStressData(member);
//            int endStressScore = stress.getScore();
//            /**
//             * 배변 score
//             * Poop 테이블에서 lastCreateTime 기준보다 큰 data를 가져온다.
//             * 배변 패드를 안 치운 횟수를 100에서 감점한다.
//             * poopList의 전체 횟수 - 치우지 않은 횟수 / 168 * 100
//             */
//            Long poopStressCnt = 0L;
//            poopStressCnt = getCountLastWeekList(member, lastStart, StressType.POOP);
//            poopStressCnt = getCountLastWeekList(member, lastStart, StressType.POOP);
//            int poopScore = (int) (1 - poopStressCnt / 168) * 100;
//
//            /**
//             *  산책 score
//             */
//            Long walkStressCnt = 0L;
//            walkStressCnt = getCountLastWeekList(member, lastStart, StressType.WALK);
//            int walkScore = (int) (1 - walkStressCnt / 7) * 100;
//
//            // 질병 score
//            Long diseaseStressCnt = 0L;
//            diseaseStressCnt = getCountLastWeekDiseaseList(member, lastStart);
//            int diseaseScore = (int) (100 - diseaseStressCnt * 10);
//            if (diseaseScore <= 0) diseaseScore = 0;
//
//            // 먹이 score
//            Long feedStressCnt = 0L;
//            feedStressCnt = getCountLastWeekList(member, lastStart, StressType.FEED);
//            int feedScore = (int) (1 - feedStressCnt / 14) * 100;
//
//            // 양치 score
//            Long brusingStressCnt = 0L;
//            brusingStressCnt = getCountLastWeekList(member, lastStart, StressType.BRUSHING_TEETH);
//            int brushingScore = (int) (1 - brusingStressCnt / 7) * 100;
//
//            // lastreport update
//            lastReport.update(endAffectionScore, endStressScore, poopScore, walkScore, feedScore, brushingScore, diseaseScore);
//            ReportResponse.from(lastReport);
//
//            // newreport save
//            Report newReport = reportRepository.save(
//                    new Report(member, lastReport.getWeek()+1, endAffectionScore, endStressScore)
//            );
//            try {
//              reportRepository.flush();
//            } catch (DataIntegrityViolationException e) {
//                throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
//            }
//
//        }
//    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }

}
