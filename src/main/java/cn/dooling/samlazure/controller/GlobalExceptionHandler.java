package cn.dooling.samlazure.controller;

import cn.dooling.samlazure.module.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author dooling
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseDTO<Object> unknownExceptionHandle(Exception e) {
        return new ResponseDTO<>(e);
    }
}
