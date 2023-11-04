package com.senabo.domain.report.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByMemberId(Member memberId);

    List<Report> deleteByMemberId(Member memberId);

    @Query("SELECT r FROM Report r WHERE r.updateTime = (SELECT MAX(r2.updateTime) FROM Report r2 WHERE r2.memberId = ?1)")
    Report findLatestData(Member memberId);

    @Query("select r from Report r where r.memberId = ?1 and r.complete = true")
    List<Report> findCompleteReport(Member member);

    @Query("select r from Report r where r.memberId = ?1 and r.complete = true and r.week = ?2")
    Optional<Report> findCompleteReportWeek(Member member, int week);

//    Optional<Report> findByMemberIdAndWeek(Member member, int week);
}