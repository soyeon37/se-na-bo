package com.senabo.domain.member.dto.response;

import com.senabo.domain.member.entity.Disease;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record DiseaseResponse(
        Long id,
        Long memberId,
        String diseaseName,
        LocalDateTime createTime,
        LocalDateTime updateTime
) {
    public static DiseaseResponse from (Disease disease){
        return DiseaseResponse.builder()
                .id(disease.getId())
                .memberId(disease.getMemberId().getId())
                .diseaseName(disease.getDiseaseName())
                .createTime(disease.getCreateTime())
                .updateTime(disease.getUpdateTime())
                .build();
    }
}
