package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalkRepository extends JpaRepository<Walk, String> {
    List<Walk> findByMemberId(Member memberId);

    List<Walk> deleteByMemberId(Member memberId);

    @Query("SELECT w FROM Walk w WHERE w.updateTime = (SELECT MAX(w2.updateTime) FROM Walk w2 WHERE w2.memberId = ?1)")
    Walk findLatestData(Member memberId);
}
