package com.senabo.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateInfoRequest (
        @NotNull
        @Schema(description = "이름", nullable = false, example = "두부")
        String name,
        @NotNull
        @Schema(description = "강아지 종", nullable = false, example = "cogi")
        String species,
        @NotNull
        @Schema(description = "성별", nullable = false, example = "F")
        String sex,
        @Schema(description = "위도", nullable = true, example = "0")
        BigDecimal houseLatitude,
        @Schema(description = "경도", nullable = true, example = "0")
        BigDecimal houseLogitude
) {

}