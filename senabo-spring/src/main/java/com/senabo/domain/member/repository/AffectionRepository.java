package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Affection;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffectionRepository extends JpaRepository<Affection, String> {
    List<Affection> findByMemberId(Member memberId);

    List<Affection> deleteByMemberId(Member memberId);
}