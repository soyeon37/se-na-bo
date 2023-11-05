package com.senabo.domain.poop.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.poop.entity.Poop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


public interface PoopRepositoryCustom {
    List<Poop> findByMemberId(Member member);

    Poop findLatestData(Member member);

    List<Poop> findLastWeekData(Member member, LocalDateTime lastStart);

    List<Poop> findPoopWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
