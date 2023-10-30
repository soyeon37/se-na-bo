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

    @Column(name = "poop_score")
    private int poopScore;

    @Column(name = "brushing_teeth_score")
    private int brushingTeethScore;

    @Column(name = "walk_score")
    private int walkScore;

    @Column(name = "food_score")
    private int foodScore;

    @Column(name = "shower_score")
    private int showerScore;
}