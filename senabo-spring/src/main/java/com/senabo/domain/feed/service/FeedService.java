package com.senabo.domain.feed.service;

import com.senabo.domain.feed.dto.response.CheckFeedResponse;
import com.senabo.domain.feed.dto.response.FeedResponse;
import com.senabo.domain.feed.entity.Feed;
import com.senabo.domain.feed.repository.FeedRepository;
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
public class FeedService {
    private final FeedRepository feedRepository;
    private final MemberService memberService;
    private final ReportService reportService;

    @Transactional
    public FeedResponse createFeed(String email) {
        Member member = memberService.findByEmail(email);
        Feed feed = feedRepository.save(
                new Feed(member));
        try {
            feedRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserException(String.valueOf(ExceptionMessage.FAIL_SAVE_DATA));
        }
        return FeedResponse.from(feed);
    }

    @Transactional
    public List<Feed> getFeed(String email) {
        Member member = memberService.findByEmail(email);
        List<Feed> feedList = feedRepository.findByMemberId(member);
        return feedList;
    }

    @Transactional
    public List<Feed> getFeedWeek(String email, int week) {
        List<Feed> feedList = new ArrayList<>();
        Member member = memberService.findByEmail(email);
        Optional<Report> result = reportService.findReportWeek(member, week);
        if (result.isEmpty()) return feedList;
        Report report = result.get();
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        feedList = feedRepository.findFeedWeek(member, endTime, startTime);
        return feedList;
    }

    @Transactional
    public void removeFeed(String email) {
        try {
            Member member = memberService.findByEmail(email);
            List<Feed> list = feedRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public CheckFeedResponse checkLastFeed(String email) {
        Member member = memberService.findByEmail(email);
        if (!feedRepository.existsByMemberId(member)) {
            LocalDateTime nowH = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
            return CheckFeedResponse.from(true, nowH, nowH);
        }
        Feed feed = feedRepository.findLatestData(member);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowH = now.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime lastFeedH = feed.getCreateTime().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime twelveAfter = lastFeedH.plusHours(12);
        return CheckFeedResponse.from(now.isAfter(twelveAfter), lastFeedH, nowH);
    }

//    @Transactional
//    public void scheduleFeed(String email) {
//        Member member = memberService.findByEmail(email);
//        Feed feed = feedRepository.findLatestData(member);
//        int originStress = member.getStressLevel();
//
//        if (feed == null || originStress == 100) return;
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime nowH = now.truncatedTo(ChronoUnit.HOURS);
//        LocalDateTime lastFeedH = feed.getCreateTime().truncatedTo(ChronoUnit.HOURS);
//
//        LocalDateTime twelveAfter = lastFeedH.plusHours(12);
//        LocalDateTime fifteenAfter = lastFeedH.plusHours(15);
//
//        log.info("현재 시각: " + nowH);
//        log.info("마지막으로 먹이준 시간: " + lastFeedH);
//        log.info("마지막으로 먹이준 시간 + 12: " + twelveAfter);
//
//
//        // 배식 12시간 경과
//        if (nowH.equals(twelveAfter)) {
//            log.info("배식 후 12시간 경과: 밥 푸시 알림 실행");
//            // 밥 푸시 알림
//            /*
//
//             */
//        }
//        // 배식 13시간 경과 이후 : 스트레스 1 씩 증가
//        else if (nowH.isAfter(twelveAfter)) {
//            log.info("배식 후 13시간 경과: 스트레스 증가");
//            // 스트레스 1 증가
//            Duration duration = Duration.between(nowH, lastFeedH);
//            long hours = duration.toHours();
//            int changeAmount = 1;
//            // 배식 15시간 경과 : 스트레스 3 증가 (1회)
//            if (nowH.isEqual(fifteenAfter)) {
//                log.info("배식 " + hours + "시간 경과: 공복 토 푸시 알림 및 스트레스 3 증가");
//                // 공복 토 푸시 알림
//                /*
//
//                 */
//                // 스트레스 3 증가
//                changeAmount = 3;
//            }
//            saveStress(member, StressType.FEED, changeAmount);
//        }
//
//    }


}
