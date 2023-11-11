package com.senabo.exception.model;

import com.senabo.exception.message.ExceptionMessage;

public class DataException extends RuntimeException{
    public DataException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.message());
    }
}
