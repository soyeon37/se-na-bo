package com.senabo.domain.stress.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.senabo.domain.member.entity.Member;
import com.senabo.domain.stress.entity.Stress;
import com.senabo.domain.stress.entity.StressType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.senabo.domain.poop.entity.QPoop.poop;
import static com.senabo.domain.stress.entity.QStress.stress;

@RequiredArgsConstructor
public class StressRepositoryImpl implements StressRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Stress> findByMemberId(Member member) {
        return queryFactory
                .selectFrom(stress)
                .where(stress.memberId.eq(member))
                .orderBy(stress.createTime.desc())
                .fetch();
    }

    @Override
    public List<Stress> findLastWeekData(Member member, LocalDateTime lastStart, StressType type) {
        return queryFactory
                .selectFrom(stress)
                .where(
                        stress.memberId.eq(member),
                        stress.createTime.goe(lastStart),
                        stress.type.eq(type)
                )
                .orderBy(stress.createTime.desc())
                .fetch();
    }

    @Override
    public Long countLastWeekData(Member member, LocalDateTime lastStart, StressType type) {
        return queryFactory
                .select(stress.count())
                .from(stress)
                .where(
                        stress.memberId.eq(member),
                        stress.createTime.goe(lastStart),
                        stress.type.eq(type)
                )
                .orderBy(stress.createTime.desc())
                .fetchOne();
    }

    @Override
    public Stress findLatestData(Member member) {
        return queryFactory
                .selectFrom(stress)
                .where(stress.memberId.eq(member))
                .orderBy(stress.updateTime.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<Stress> findStressWeek(Member member, LocalDateTime endTime, LocalDateTime startTime) {
        return queryFactory
                .selectFrom(stress)
                .where(
                        stress.memberId.eq(member),
                        stress.updateTime.loe(endTime),
                        stress.createTime.goe(startTime)
                )
                .orderBy(stress.createTime.desc())
                .fetch();
    }
}