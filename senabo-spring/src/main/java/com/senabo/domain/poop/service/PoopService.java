package com.senabo.domain.poop.service;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.domain.poop.dto.response.PoopResponse;
import com.senabo.domain.poop.entity.Poop;
import com.senabo.domain.poop.repository.PoopRepository;
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
public class PoopService {
        private final PoopRepository poopRepository;
        private final ReportRepository reportRepository;
        private final MemberRepository memberRepository;

    @Transactional
    public PoopResponse createPoop(Long id) {
        Member member = findById(id);
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
    public List<Poop> getPoop(Long id) {
        Member member = findById(id);
        List<Poop> poopList = poopRepository.findByMemberId(member);
        if (poopList.isEmpty()) {
            throw new EntityNotFoundException("Poop에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return poopList;
    }
    @Transactional
    public List<Poop> getPoopWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Poop> poopList = poopRepository.findPoopWeek(member, endTime, startTime);
        if (poopList.isEmpty()) {
            throw new EntityNotFoundException("Poop에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return poopList;
    }

    @Transactional
    public void removePoop(Long id) {
        try {
            Member member = findById(id);
            List<Poop> list = poopRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public PoopResponse updatePoop(Long id) {
        Member member = findById(id);
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


    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
