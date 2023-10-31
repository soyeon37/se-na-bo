package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Bath;
import com.senabo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BathRepository extends JpaRepository<Bath, String> {
    List<Bath> findByMemberId(Member memberId);

    List<Bath> deleteByMemberId(Member memberId);
}
