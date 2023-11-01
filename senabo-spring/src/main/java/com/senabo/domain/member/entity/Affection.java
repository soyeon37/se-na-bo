package com.senabo.domain.member.entity;

import com.senabo.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Affection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Column(name = "type")
    private AffectionType type;

    @Column(name = "change_amount")
    private int changeAmount;

    @Column(name = "score")
    private int score;

    public Affection(Member memberId, AffectionType type, int changeAmount, int score) {
        this.memberId = memberId;
        this.type = type;
        this.changeAmount = changeAmount;
        this.score = score;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public AffectionType getType() {
        return type;
    }

    public void setType(AffectionType type) {
        this.type = type;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
