package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BrushingTeethRepository extends JpaRepository<BrushingTeeth, String> {
    List<BrushingTeeth> findByMemberId(Member memberId);

    List<BrushingTeeth> deleteByMemberId(Member memberId);
}
