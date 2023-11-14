package com.senabo.domain.member.repository;

import com.senabo.domain.member.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findAllMemberNonComplete();
}
