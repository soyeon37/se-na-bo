package com.senabo.domain.poop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.poop.entity.Poop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import static com.senabo.domain.poop.entity.QPoop.poop;

@RequiredArgsConstructor
public class PoopRepositoryImpl implements PoopRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Poop> findByMemberId(Member member) {
        return queryFactory
                .selectFrom(poop)
                .where(poop.memberId.eq(member))
                .orderBy(poop.createTime.desc())
                .fetch();
    }

    @Override
    public Poop findLatestData(Member member) {
        return queryFactory
                .selectFrom(poop)
                .where(poop.memberId.eq(member))
                .orderBy(poop.updateTime.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<Poop> findLastWeekData(Member member, LocalDateTime lastStart) {
        return queryFactory
                .selectFrom(poop)
                .where(
                        poop.memberId.eq(member),
                        poop.createTime.goe(lastStart)
                )
                .orderBy(poop.createTime.desc())
                .fetch();
    }

    @Override
    public List<Poop> findPoopWeek(Member member, LocalDateTime endTime, LocalDateTime startTime) {
        return queryFactory
                .selectFrom(poop)
                .where(
                        poop.memberId.eq(member),
                        poop.updateTime.loe(endTime),
                        poop.createTime.goe(startTime)
                )
                .orderBy(poop.createTime.desc())
                .fetch();
    }
}
