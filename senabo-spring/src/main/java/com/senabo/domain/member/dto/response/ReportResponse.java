package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Member;
import com.senabo.domain.member.entity.Report;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportResponse(
        Long id,
        Long memberId,
        int week,
        int totalTime,
        int startAffectionScore,
        int startStressScore,
        int endAffectionScore,
        int endStressScore,
        int poopScore,
        int walkScore,
        int feedScore,
        int brushingTeethScore,
        int diseaseScore,
        Boolean complete,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
    public static ReportResponse from(Report report){
        return ReportResponse.builder()
                .id(report.getId())
                .memberId(report.getMemberId().getId())
                .week(report.getWeek())
                .totalTime(report.getTotalTime())
                .startAffectionScore(report.getStartAffectionScore())
                .startStressScore(report.getStartStressScore())
                .endAffectionScore(report.getEndAffectionScore())
                .endStressScore(report.getEndStressScore())
                .poopScore(report.getPoopScore())
                .walkScore(report.getWalkScore())
                .feedScore(report.getFeedScore())
                .brushingTeethScore(report.getBrushingTeethScore())
                .diseaseScore(report.getDiseaseScore())
                .complete(report.getComplete())
                .createTime(report.getCreateTime())
                .updateTime(report.getUpdateTime())
                .build();
    }
}
