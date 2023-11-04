package com.senabo.domain.poop.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.service.MemberService;
import com.senabo.domain.poop.dto.response.PoopResponse;
import com.senabo.domain.poop.entity.Poop;
import com.senabo.domain.poop.repository.PoopRepository;
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
public class PoopService {
    private final PoopRepository poopRepository;
    private final ReportService reportService;
    private final MemberService memberService;

    @Transactional
    public PoopResponse createPoop(String email) {
        Member member = memberService.findByEmail(email);
        Poop poop = poopRepository.save(
                new Poop(member, false));
        try {
            poopRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return PoopResponse.from(poop);
    }


    @Transactional
    public List<Poop> getPoop(String email) {
        Member member = memberService.findByEmail(email);
        List<Poop> poopList = poopRepository.findByMemberId(member);
        return poopList;
    }

    @Transactional
    public List<Poop> getPoopWeek(String email, int week) {
        List<Poop> poopList = new ArrayList<>();
        Member member = memberService.findByEmail(email);
        Optional<Report> result = reportService.findReportWeek(member, week);
        if (result.isEmpty()) return poopList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        poopList = poopRepository.findPoopWeek(member, endTime, startTime);
        return poopList;
    }

    @Transactional
    public void removePoop(String email) {
        try {
            Member member = memberService.findByEmail(email);
            List<Poop> list = poopRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public PoopResponse updatePoop(String email) {
        Member member = memberService.findByEmail(email);
        Poop poop = poopRepository.findLatestData(member);
        if (!poop.getCleanYn()) {
            poop.update(true);
        }
        return PoopResponse.from(poop);
    }

//    @Transactional
//    public void schedulePoop(Long id) {
//        Member member = findById(id);
//        int originStress = member.getStressLevel();
//        Poop poop = poopRepository.findLatestData(member);
//
//        if (originStress == 100 || poop.getCleanYn()) return;
//
//        Map<String, Object> map = checkLastFeed(1L);
//        LocalDateTime lastFeedH = (LocalDateTime) map.get("lastFeedDateTime");
//        LocalDateTime nowH = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
//        log.info("마지막 밥 제공 시간: " + lastFeedH);
//        LocalDateTime threeAfter = lastFeedH.plusHours(3);
//        if (nowH.isEqual(threeAfter)) {
//            // 배변 활동 푸시 알림
//            /*
//
//             */
//        } else if (nowH.isAfter(threeAfter)) {
//            // 스트레스 1 증가
//            Duration duration = Duration.between(nowH, lastFeedH);
//            long hours = duration.toHours();
//            log.info("배변 후 " + hours + "시간 경과: 스트레스 1 증가");
//            int changeAmount = 1;
//
//            saveStress(member, StressType.POOP, changeAmount);
//        }
//    }

}
