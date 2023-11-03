package com.senabo.domain.communication.repository;

import com.senabo.domain.communication.entity.Communication;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface CommunicationRepository extends JpaRepository<Communication, String> {
    List<Communication> findByMemberId(Member memberId);

    List<Communication> deleteByMemberId(Member memberId);

    @Query("select c from Communication c where c.memberId = ?1 and c.updateTime <= ?2 and c.createTime >= ?3 ")
    List<Communication> findCommunicationWeek(Member member, LocalDateTime endTime, LocalDateTime startTime);
}
