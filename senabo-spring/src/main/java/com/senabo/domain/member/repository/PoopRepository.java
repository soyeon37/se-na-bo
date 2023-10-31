package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Poop;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PoopRepository extends JpaRepository<Poop, String> {
    List<Poop> findByMemberId(Member memberId);

    List<Poop> deleteByMemberId(Member memberId);

    @Query("SELECT p FROM Poop p WHERE p.updateTime = (SELECT MAX(p2.updateTime) FROM Poop p2 WHERE p2.memberId = ?1)")
    Poop findLatestData(Member memberId);

}
