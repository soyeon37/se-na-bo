package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Expense;
import com.senabo.domain.member.entity.Feed;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, String> {
    List<Feed> findByMemberId(Member memberId);

    List<Feed> deleteByMemberId(Member memberId);

    @Query("SELECT f FROM Feed f WHERE f.updateTime = (SELECT MAX(f2.updateTime) FROM Feed f2 WHERE f2.memberId = ?1)")
    Feed findLatestData(Member memberId);

    @Query("select f from Feed f where f.memberId = ?1 and f.updateTime <= ?2 and f.createTime >= ?3 ")
    List<Feed> findFeedWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
