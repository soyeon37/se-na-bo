package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Feed;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Stress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StressRepository extends JpaRepository<Stress, String> {
    List<Stress> findByMemberId(Member memberId);

    List<Stress> deleteByMemberId(Member memberId);
}
