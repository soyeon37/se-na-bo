package com.senabo.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    FAIL_DELETE_DATA("삭제에 실패했습니다."),

    FAIL_UPDATE_DATA("수정에 실패했습니다."),

    FAIL_SAVE_DATA("저장에 실패했습니다."),

    MISMATCH_USE_TOKEN(HttpStatus.SC_UNAUTHORIZED,"유저와 토큰이 맞지 않습니다."),

    NOT_AUTHORIZED_ACCESS(HttpStatus.SC_UNAUTHORIZED,"인증되지 않은 접근입니다."),

    FAIL_TOKEN_CHECK(HttpStatus.SC_UNAUTHORIZED,"토큰 검증에 실패했습니다."),

    TOKEN_VALID_TIME_EXPIRED(HttpStatus.SC_UNAUTHORIZED,"토큰의 유효기간이 만료되었습니다."),

    AUTH_NOT_FOUND(HttpStatus.SC_UNAUTHORIZED,"권한 정보가 없는 토큰입니다."),

    MISMATCH_TOKEN(HttpStatus.SC_UNAUTHORIZED, "토큰명이 일치하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "유저를 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "토큰을 찾을 수 없습니다."),
    DATA_NOT_FOUND(HttpStatus.SC_NOT_FOUND, "데이터를 찾을 수 없습니다.");

    private final String message;
    private int httpStatus;

    ExceptionMessage(int httpStatus, String message) {this.httpStatus = httpStatus; this.message = message;}
    ExceptionMessage(String message) {this.message = message;}


    public String message(){ return message;}

}
