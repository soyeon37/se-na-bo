package com.senabo.exception.model;


import com.senabo.exception.message.ExceptionMessage;

public class UserAuthException extends RuntimeException{

    public UserAuthException(String error) {super(error);}

    public UserAuthException(ExceptionMessage exceptionMessage) {super(exceptionMessage.message());}
}
