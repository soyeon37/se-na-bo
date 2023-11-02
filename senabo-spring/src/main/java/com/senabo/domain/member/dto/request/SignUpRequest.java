package com.senabo.domain.member.dto.request;

import com.senabo.domain.member.entity.SNSType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SignUpRequest(
        @NotNull
        @Schema(description = "이름", nullable = false, example = "두부")
        String name,
        @NotNull
        @Schema(description = "이메일", nullable = false, example = "ssafy@naver.com")
        String email,
        @NotNull
        @Schema(description = "강아지 종", nullable = false, example = "cogi")
        String species,
        @NotNull
        @Schema(description = "성별", nullable = false, example = "F")
        String sex,
        @Schema(description = "위도", nullable = true, example = "0")
        BigDecimal houseLatitude,
        @Schema(description = "경도", nullable = true, example = "0")
        BigDecimal houseLogitude,
        String uid,
        String deviceToken


) {

}
