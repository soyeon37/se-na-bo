package com.senabo.exception.handler;

import com.senabo.exception.dto.ExceptionDto;
import com.senabo.exception.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ExceptionDto> exceptionHandler(Exception exception){
//        return ExceptionDto.
//    }
    @ExceptionHandler(DataException.class)
    protected ResponseEntity<?> handleDataException(DataException exception) {
        exception.printStackTrace();
        return ExceptionDto.toResponseEntity(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(TokenCheckFailException.class)
    protected ResponseEntity<?> handleTokenCheckFailException(TokenCheckFailException exception) {
        exception.printStackTrace();
        return ExceptionDto.toResponseEntity(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    protected ResponseEntity<?> handleTokenNotFoundException(TokenNotFoundException exception) {
        exception.printStackTrace();
        return ExceptionDto.toResponseEntity(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(UserAuthException.class)
    protected ResponseEntity<?> handleUserAuthException(UserAuthException exception) {
        exception.printStackTrace();
        return ExceptionDto.toResponseEntity(exception.getHttpStatus(), exception.getMessage());
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<?> handleUserException(UserException exception) {
        exception.printStackTrace();
        return ExceptionDto.toResponseEntity(exception.getHttpStatus(), exception.getMessage());
    }


}
