package com.senabo.domain.member.entity;

import com.senabo.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Column(name = "week")
    private int week;

    @Column(name = "affection_score")
    private int affectionScore;

    @Column(name = "stress_score")
    private int stressScore;

    public Report(Member memberId, int week, int affectionScore, int stressScore) {
        this.memberId = memberId;
        this.week = week;
        this.affectionScore = affectionScore;
        this.stressScore = stressScore;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getAffectionScore() {
        return affectionScore;
    }

    public void setAffectionScore(int affectionScore) {
        this.affectionScore = affectionScore;
    }

    public int getStressScore() {
        return stressScore;
    }

    public void setStressScore(int stressScore) {
        this.stressScore = stressScore;
    }
}
