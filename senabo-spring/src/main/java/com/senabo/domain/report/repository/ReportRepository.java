package com.senabo.domain.report.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByMemberId(Member memberId);

    List<Report> deleteByMemberId(Member memberId);


    @Query("SELECT r FROM Report r WHERE r.updateTime = (SELECT MAX(r2.updateTime) FROM Report r2 WHERE r2.memberId = ?1)")
    Report findLatestData(Member memberId);

    @Query("select r from Report r where r.memberId = ?1 and r.updateTime <= ?2 and r.createTime >= ?3 ")
    List<Report> findReportWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
    Report findByMemberIdAndWeek(Member member, int week);
}