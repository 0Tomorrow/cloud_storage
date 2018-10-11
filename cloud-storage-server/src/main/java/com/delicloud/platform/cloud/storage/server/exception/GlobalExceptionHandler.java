//package com.delicloud.platform.cloud.storage.server.exception;
//
//import com.delicloud.platform.common.lang.bo.RespBase;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpRequest;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//
//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(value = MyException.class)
//    @ResponseBody
//    public RespBase<?> MyExceptionHandler(HttpServletRequest request, MyException e) {
//        log.error("接口[{}]请求异常, msg : {}", request.getRequestURI(), e.getMessage());
//        RespBase<?> response = new RespBase<>(e.getCode(), e.getMessage());
//        return response;
//    }
//}
