package com.senabo.exception.handler;

import com.senabo.exception.dto.ExceptionDto;
import com.senabo.exception.message.ExceptionMessage;
import com.senabo.exception.model.TokenCheckFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ExceptionDto> exceptionHandler(Exception exception){
//        return ExceptionDto.
//    }
//    @ExceptionHandler(TokenCheckFailException.class)
//    protected ResponseEntity<ExceptionDto> handleTokenCheckFailException(ExceptionMessage exceptionMessage){
//        return ExceptionDto.toResponseEntity(exceptionMessage);
//    }


}
