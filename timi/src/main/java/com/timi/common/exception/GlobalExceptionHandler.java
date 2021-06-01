package com.timi.common.exception;

import com.timi.common.bean.ResponseBean;
import com.timi.common.code.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * @author hhh
 * @date 2021/6/1
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 业务异常
     * @param exception
     * @return
     */
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    public ResponseBean businessHandler(Exception ex) {
        ResponseBean responseBean = null;
        log.error("系统异常", ex);
        if (ex instanceof BusinessException) {
            BusinessException be = (BusinessException) ex;
            responseBean = ResponseBean.builder().code(be.getCode()).build();
            responseBean.setMessage(messageSource.getMessage(be.getCode(), be.getArgs(), LocaleContextHolder.getLocale()));
        } else {
            responseBean = ResponseBean.builder().code(ResponseCode.FAIL_CODE).build();
            responseBean.setMessage(messageSource.getMessage(ResponseCode.FAIL_CODE, null, LocaleContextHolder.getLocale()));
        }
        return responseBean;
    }


    /**
     * 参数校验异常
     *
     * @param methodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBean methodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        String code = methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage();
        String message;
        try {

            message = messageSource.getMessage(code, null,LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException exception) {
            message = code;
        }
        return ResponseBean.builder().code(ResponseCode.FAIL_CODE).message(message).build();
    }

}
