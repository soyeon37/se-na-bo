package com.senabo.domain.member.repository;

import com.senabo.domain.member.dto.response.BrushingTeethResponse;
import com.senabo.domain.member.entity.BrushingTeeth;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BrushingTeethRepository extends JpaRepository<BrushingTeeth, String>, BrushingTeethRepositoryCustom {
    List<BrushingTeeth> findByMemberId(Member memberId);

    List<BrushingTeeth> deleteByMemberId(Member memberId);

    @Query("select b from BrushingTeeth b where b.memberId = ?1 and b.updateTime <= ?2 and b.createTime >= ?3 ")
    List<BrushingTeeth> findBrushingTeethWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
