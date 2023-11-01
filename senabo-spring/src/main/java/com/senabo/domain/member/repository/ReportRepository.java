package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Report;
import com.senabo.domain.member.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByMemberId(Member memberId);

    List<Report> deleteByMemberId(Member memberId);


    @Query("SELECT r FROM Report r WHERE r.updateTime = (SELECT MAX(r2.updateTime) FROM Report r2 WHERE r2.memberId = ?1)")
    Report findLatestData(Member memberId);


    Report findByMemberIdAndWeek(Member member, int week);
}