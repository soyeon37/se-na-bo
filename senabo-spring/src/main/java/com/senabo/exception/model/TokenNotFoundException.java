package com.senabo.exception.model;


import com.senabo.exception.message.ExceptionMessage;

public class TokenNotFoundException extends RuntimeException{
    public TokenNotFoundException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }
}
