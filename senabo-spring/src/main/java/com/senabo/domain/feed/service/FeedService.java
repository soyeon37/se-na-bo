package com.senabo.domain.feed.service;

import com.senabo.domain.feed.dto.response.FeedResponse;
import com.senabo.domain.feed.entity.Feed;
import com.senabo.domain.feed.repository.FeedRepository;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
import com.senabo.domain.member.repository.MemberRepository;
import com.senabo.domain.report.repository.ReportRepository;
import com.senabo.domain.stress.entity.StressType;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.UserException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public FeedResponse createFeed(Long id) {
        Member member = findById(id);
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
    public List<Feed> getFeed(Long id) {
        Member member = findById(id);
        List<Feed> feedList = feedRepository.findByMemberId(member);
        if (feedList.isEmpty()) {
            throw new EntityNotFoundException("Feed에서 해당 MemberId를 찾을 수 없습니다.: " + id);
        }
        return feedList;
    }

    @Transactional
    public List<Feed> getFeedWeek(Long id, int week) {
        Member member = findById(id);
        Report report = reportRepository.findByMemberIdAndWeek(member, week);
        LocalDateTime startTime = report.getCreateTime().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = report.getUpdateTime().truncatedTo(ChronoUnit.DAYS).plusDays(1);
        List<Feed> feedList = feedRepository.findFeedWeek(member, endTime, startTime);
        if (feedList.isEmpty()) {
            throw new EntityNotFoundException("Feed에서 해당 주차를 찾을 수 없습니다.: " + id);
        }
        return feedList;
    }

    @Transactional
    public void removeFeed(Long id) {
        try {
            Member member = findById(id);
            List<Feed> list = feedRepository.deleteByMemberId(member);
        } catch (DataIntegrityViolationException e) {
            throw new UserException(ExceptionMessage.FAIL_DELETE_DATA);
        }
    }

    @Transactional
    public Map<String, Object> checkLastFeed(Long id) {
        Map<String, Object> result = new HashMap<>();
        Member member = findById(id);
        Feed feed = feedRepository.findLatestData(member);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowH = now.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime lastFeedH = feed.getCreateTime().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime twelveAfter = lastFeedH.plusHours(12);
        if (nowH.isBefore(twelveAfter)) {
            result.put("possibleYn", false);
        } else {
            result.put("possibleYn", true);
        }
        result.put("lastFeedDateTime", lastFeedH);
        result.put("nowDateTime", nowH);
        return result;
    }

//    @Transactional
//    public void scheduleFeed(Long id) {
//        Member member = findById(id);
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

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));
    }
}
