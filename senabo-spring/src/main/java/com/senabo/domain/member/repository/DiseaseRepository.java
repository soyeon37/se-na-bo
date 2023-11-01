package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Disease;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.StressType;
import com.senabo.domain.member.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, String> {
    List<Disease> findByMemberId(Member memberId);

    List<Disease> deleteByMemberId(Member memberId);

    @Query("SELECT COUNT(d) FROM Disease d WHERE d.memberId = ?1 and d.createTime >= ?2")
    List<Disease> findLastWeekData(Member memberId, LocalDateTime lastStart);

    @Query("SELECT COUNT(d) FROM Disease d WHERE d.memberId = ?1 and d.createTime >= ?2")
    Long countLastWeekData(Member memberId, LocalDateTime lastStart);
}
