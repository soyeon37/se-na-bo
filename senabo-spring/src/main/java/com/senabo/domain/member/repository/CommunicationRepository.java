package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Communication;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Poop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommunicationRepository extends JpaRepository<Communication, String> {
    List<Communication> findByMemberId(Member memberId);

    List<Communication> deleteByMemberId(Member memberId);
}
