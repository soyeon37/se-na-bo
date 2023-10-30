package com.senabo.exception.model;


import com.senabo.exception.message.ExceptionMessage;

public class TokenCheckFailException extends RuntimeException{
    public TokenCheckFailException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }
}
