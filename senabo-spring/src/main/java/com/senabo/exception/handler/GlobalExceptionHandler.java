package com.senabo.exception.handler;

import com.senabo.exception.dto.ExceptionDto;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ExceptionDto> exceptionHandler(Exception exception){
//        return ExceptionDto.
//    }
    @ExceptionHandler({DataException.class, TokenCheckFailException.class, TokenNotFoundException.class, UserAuthException.class, UserException.class})
    protected ResponseEntity<ExceptionDto> handle(ExceptionMessage exceptionMessage){
        return ExceptionDto.toResponseEntity(exceptionMessage);
    }


}
