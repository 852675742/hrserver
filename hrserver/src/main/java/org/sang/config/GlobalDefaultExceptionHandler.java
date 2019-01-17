package org.sang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * web  全局异常回调
 * @author rick on 2018/07/22 10:25.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);



    /**
     * spring mvc参数检查@Valid
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMesssage = new StringBuilder("参数错误:");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage.append(fieldError.getField() + fieldError.getDefaultMessage());
            errorMesssage.append(";");
        }
        logger.error("异常:", ex);
        Map map = new HashMap<String, String>();
        map.put("errorCode", "E9999");
        map.put("errorMsg", errorMesssage.toString());
        return map;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Map handleBindException(
            BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMesssage = new StringBuilder("参数错误:");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage.append(fieldError.getField() + fieldError.getDefaultMessage());
            errorMesssage.append(";");
        }
        logger.error("异常:", ex);
        Map map = new HashMap<String, String>();
        map.put("errorCode", "E9999");
        map.put("errorMsg", errorMesssage.toString());
        return map;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Map handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        logger.error("异常:", ex);
        Map map = new HashMap<String, String>();
        map.put("errorCode", "E9999");
        map.put("errorMsg", "缺少参数:" + parameterName);
        return map;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map defaultErrorHandler(Exception ex){
        logger.error(ex.getMessage(),ex);
        Map map = new HashMap<String, String>();
        map.put("errorCode", "E9999");
        map.put("errorMsg", ex.getMessage());
        return map;
    }

}
