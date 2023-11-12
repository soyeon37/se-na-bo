package com.senabo.exception.model;


import com.senabo.exception.message.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class TokenCheckFailException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
    public TokenCheckFailException(ExceptionMessage exceptionMessage) {
      this.httpStatus = exceptionMessage.getHttpStatus();
      this.message = exceptionMessage.getMessage();
    }
}
