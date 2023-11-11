//package com.senabo.exception.handler;
//
//import com.senabo.exception.dto.ExceptionDto;
//import com.senabo.exception.message.ExceptionMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//@Slf4j
//public class ExceptionHandler {
//
////    @ExceptionHandler(value = Exception.class)
////    public ResponseEntity<ExceptionDto> exceptionHandler(Exception exception){
////        return ExceptionDto.
////    }
//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<ErrorResponseEntity> handleCustomException(ExceptionMessage e){
//        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
//    }
//
//
//}
