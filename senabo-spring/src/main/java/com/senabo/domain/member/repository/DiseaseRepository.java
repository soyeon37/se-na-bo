package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Disease;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, String> {
    List<Disease> findByMemberId(Member memberId);

    List<Disease> deleteByMemberId(Member memberId);
}
