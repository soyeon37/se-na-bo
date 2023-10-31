package com.senabo.domain.member.entity;

import com.senabo.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Stress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Column(name = "poop_score")
    private int poopScore;

    @Column(name = "brushing_teeth_score")
    private int brushingTeethScore;

    @Column(name = "walk_score")
    private int walkScore;

    @Column(name = "feed_score")
    private int feedScore;

    @Column(name = "shower_score")
    private int showerScore;

    public Stress(Member memberId, int poopScore, int brushingTeethScore, int walkScore, int feedScore, int showerScore) {
        this.memberId = memberId;
        this.poopScore = poopScore;
        this.brushingTeethScore = brushingTeethScore;
        this.walkScore = walkScore;
        this.feedScore = feedScore;
        this.showerScore = showerScore;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public int getPoopScore() {
        return poopScore;
    }

    public void setPoopScore(int poopScore) {
        this.poopScore = poopScore;
    }

    public int getBrushingTeethScore() {
        return brushingTeethScore;
    }

    public void setBrushingTeethScore(int brushingTeethScore) {
        this.brushingTeethScore = brushingTeethScore;
    }

    public int getWalkScore() {
        return walkScore;
    }

    public void setWalkScore(int walkScore) {
        this.walkScore = walkScore;
    }

    public int getFeedScore() {
        return feedScore;
    }

    public void setFeedScore(int feedScore) {
        this.feedScore = feedScore;
    }

    public int getShowerScore() {
        return showerScore;
    }

    public void setShowerScore(int showerScore) {
        this.showerScore = showerScore;
    }
}