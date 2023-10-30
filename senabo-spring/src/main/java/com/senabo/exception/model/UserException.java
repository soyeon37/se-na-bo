package com.senabo.exception.model;


import com.senabo.exception.message.ExceptionMessage;

public class UserException extends RuntimeException {

    public UserException(String error){super(error);}

    public UserException(ExceptionMessage exceptionMessage){
        super(exceptionMessage.message());
    }
}
