package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Poop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PoopRepository extends JpaRepository<Poop, String> {
    List<Poop> findByMemberId(Member memberId);

    List<Poop> deleteByMemberId(Member memberId);
}
