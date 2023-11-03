package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Member;
import lombok.Builder;

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
        BigDecimal houseLongitude,
        int totalTime,
        String uid,
        String deviceToken,
        LocalDateTime exitTime,
        LocalDateTime enterTime,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .species(member.getSpecies())
                .sex(member.getSex())
                .houseLatitude(member.getHouseLatitude())
                .houseLongitude(member.getHouseLongitude())
                .uid(member.getUid())
                .deviceToken(member.getDeviceToken())
                .affection(member.getAffection())
                .stressLevel(member.getStressLevel())
                .totalTime(member.getTotalTime())
                .exitTime(member.getExitTime())
                .enterTime(member.getEnterTime())
                .createTime(member.getCreateTime())
                .updateTime(member.getUpdateTime())
                .build();
    }
}
