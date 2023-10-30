package com.senabo.domain.member.entity;

import com.senabo.common.audit.BaseEntity;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "species", nullable = false)
    private String species;

    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "affection")
    private int affection;

    @Column(name = "stress_level")
    private int stressLevel;

    @Column(name = "house_latitude", nullable = false)
    private BigDecimal houseLatitude;

    @Column(name = "house_logitude", nullable = false)
    private BigDecimal houseLogitude;

    @Column(name = "sns_type", nullable = false)
    private SNSType snsType;

    @Column(name = "total_time")
    private int totalTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "enter_time")
    private LocalDateTime enterTime;

    public Member(String name, String email, String species, String sex, BigDecimal houseLatitude, BigDecimal houseLogitude, SNSType snsType) {
        this.name = name;
        this.email = email;
        this.species = species;
        this.sex = sex;
        this.houseLatitude = houseLatitude;
        this.houseLogitude = houseLogitude;
        this.snsType = snsType;
        affection = 0;
        stressLevel = 0;
    }
}
