package com.senabo.domain.poop.repository;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.poop.entity.Poop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface PoopRepository extends JpaRepository<Poop, String>, PoopRepositoryCustom {
    List<Poop> deleteByMemberId(Member member);
}
