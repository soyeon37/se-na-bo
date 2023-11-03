package com.senabo.domain.poop.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.poop.entity.Poop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PoopRepository extends JpaRepository<Poop, String> {
    List<Poop> findByMemberId(Member memberId);

    List<Poop> deleteByMemberId(Member memberId);

    @Query("SELECT p FROM Poop p WHERE p.updateTime = (SELECT MAX(p2.updateTime) FROM Poop p2 WHERE p2.memberId = ?1)")
    Poop findLatestData(Member memberId);

    @Query("SELECT p FROM Poop p WHERE p.memberId = ?1 and p.createTime >= ?2")
    List<Poop> findLastWeekData(Member memberId, LocalDateTime lastStart);
    @Query("select p from Poop p where p.memberId = ?1 and p.updateTime <= ?2 and p.createTime >= ?3 ")
    List<Poop> findPoopWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
