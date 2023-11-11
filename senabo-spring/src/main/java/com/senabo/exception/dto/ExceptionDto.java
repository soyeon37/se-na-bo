package com.senabo.exception.dto;

import com.senabo.exception.message.ExceptionMessage;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ExceptionDto {
    private final String message;
    public final int statusCode;

    public static ResponseEntity<ExceptionDto> toResponseEntity(ExceptionMessage e){

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ExceptionDto.builder()
                        .statusCode(e.getHttpStatus())
                        .message(e.getMessage())
                        .build());
    }

}
