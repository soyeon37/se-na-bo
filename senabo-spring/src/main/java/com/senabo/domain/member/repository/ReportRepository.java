package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByMemberId(Member memberId);

    List<Report> deleteByMemberId(Member memberId);
}