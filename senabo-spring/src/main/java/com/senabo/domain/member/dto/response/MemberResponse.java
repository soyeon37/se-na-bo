package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.SNSType;
import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record MemberResponse(
        Long id,
        String name,
        String email,
        String species,
        String sex,
        int affection,
        int stressLevel,
        BigDecimal houseLatitude,
        BigDecimal houseLogitude,
        SNSType snsType,
        int totalTime,
        LocalDateTime exitTime,
        LocalDateTime enterTime,
        LocalDateTime dateTime
        ) {
        public static MemberResponse from(Member member) {
                return MemberResponse.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .email(member.getEmail())
                        .species(member.getSpecies())
                        .sex(member.getSex())
                        .houseLatitude(member.getHouseLatitude())
                        .houseLogitude(member.getHouseLogitude())
                        .snsType(member.getSnsType())
                        .affection(member.getAffection())
                        .stressLevel(member.getStressLevel())
                        .totalTime(member.getTotalTime())
                        .exitTime(member.getExitTime())
                        .enterTime(member.getEnterTime())
                        .build();
        }
}
