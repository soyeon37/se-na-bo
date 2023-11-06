package com.senabo.domain.affection.repository;

import com.senabo.domain.affection.dto.response.AffectionResponse;
import com.senabo.domain.affection.entity.Affection;
import com.senabo.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface AffectionRepositoryCustom {
    List<AffectionResponse> findByMemberId(Member memberId);
    Affection findLatestDataByMemberId(Member member);
    List<AffectionResponse> findAffectionWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);

}
