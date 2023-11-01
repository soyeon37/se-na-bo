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

    @Column(name = "start_affection_score")
    private int startAffectionScore;

    @Column(name = "start_stress_score")
    private int startStressScore;

    @Column(name = "end_affection_score")
    private int endAffectionScore;

    @Column(name = "end_stress_score")
    private int endStressScore;

    @Column(name = "poop_score")
    private int poopScore;

    @Column(name = "walk_score")
    private int walkScore;

    @Column(name = "feed_score")
    private int feedScore;

    @Column(name = "brushing_teeth_score")
    private int brushingTeethScore;

    @Column(name = "disease_score")
    private int diseaseScore;

    public Report(Member memberId, int week, int startAffectionScore, int startStressScore, int endAffectionScore, int endStressScore, int poopScore, int walkScore, int feedScore, int brushingTeethScore, int diseaseScore) {
        this.memberId = memberId;
        this.week = week;
        this.startAffectionScore = startAffectionScore;
        this.startStressScore = startStressScore;
        this.endAffectionScore = endAffectionScore;
        this.endStressScore = endStressScore;
        this.poopScore = poopScore;
        this.walkScore = walkScore;
        this.feedScore = feedScore;
        this.brushingTeethScore = brushingTeethScore;
        this.diseaseScore = diseaseScore;
    }

//    public void update(){
//
//    }
}
