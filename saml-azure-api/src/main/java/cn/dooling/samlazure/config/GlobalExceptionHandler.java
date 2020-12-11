package cn.dooling.samlazure.config;

import cn.dooling.samlazure.domain.dto.ResponseDTO;
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
