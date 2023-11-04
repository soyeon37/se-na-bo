package com.senabo.domain.affection.repository;

import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AffectionRepository extends JpaRepository<Affection, String> {
    List<Affection> findByMemberId(Member memberId);

    List<Affection> deleteByMemberId(Member memberId);

    @Query("SELECT a FROM Affection a WHERE a.updateTime = (SELECT MAX(a2.updateTime) FROM Affection a2 WHERE a2.memberId = ?1)")
    Affection findLatestData(Member memberId);

    @Query("SELECT a FROM Affection a WHERE a.memberId = ?1 AND a.updateTime <= ?2 AND a.createTime >= ?3 ")
    List<Affection> findAffectionWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}