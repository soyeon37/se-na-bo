package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StressRepository extends JpaRepository<Stress, String> {
    List<Stress> findByMemberId(Member memberId);

    List<Stress> deleteByMemberId(Member memberId);

    @Query("SELECT s FROM Stress s WHERE s.memberId = ?1 and s.createTime >= ?2 and s.type = ?3")
    List<Stress> findLastWeekData(Member memberId, LocalDateTime lastStart, StressType type);

    @Query("SELECT COUNT(s) FROM Stress s WHERE s.memberId = ?1 and s.createTime >= ?2 and s.type = ?3")
    Long countLastWeekData(Member memberId, LocalDateTime lastStart, StressType type);

}
