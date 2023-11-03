package com.senabo.domain.walk.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalkRepository extends JpaRepository<Walk, String> {
    List<Walk> findByMemberId(Member memberId);

    List<Walk> deleteByMemberId(Member memberId);

    @Query("SELECT w FROM Walk w WHERE w.updateTime = (SELECT MAX(w2.updateTime) FROM Walk w2 WHERE w2.memberId = ?1)")
    Walk findLatestData(Member memberId);

    @Query("SELECT w FROM Walk w WHERE w.memberId = ?1 AND w.startTime >= ?2 AND w.endTime IS NOT NULL")
    List<Walk> findTodayData(Member memberId, LocalDateTime startToday);

    @Query("select w from Walk w where w.memberId = ?1 and w.updateTime <= ?2 and w.createTime >= ?3 ")
    List<Walk> findWalkWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
