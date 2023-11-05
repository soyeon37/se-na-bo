package com.senabo.domain.walk.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.walk.entity.Walk;

import java.time.LocalDateTime;
import java.util.List;

public interface WalkRepositoryCustom{
    List<Walk> findByMemberId(Member member);
    Walk findLatestData(Member member);
    List<Walk> findTodayData(Member member, LocalDateTime startToday);
    List<Walk> findWalkWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
