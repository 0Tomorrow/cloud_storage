package com.delicloud.platform.cloud.storage.server.aop;

import com.delicloud.platform.common.lang.exception.PlatformException;
import com.delicloud.platform.common.lang.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Pointcut("execution(public * com.delicloud.platform.cloud.storage.server.controller..*.*(..)) " +
            "&& !execution(public * com.delicloud.platform.cloud.storage.server.controller.FileController.sliceUpload(..))")
    public void controllerLogService() {
    }

    @Around("controllerLogService()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String paramJson = getRequestJson(joinPoint);
        HttpServletRequest request = getRequest();
        log.info("request [{}], param : {}", request.getRequestURI(), paramJson);
        Object result = joinPoint.proceed(args);
        String resultJson = JsonUtil.getJsonFromObject(result);
        log.info("response [{}], return : {}", request.getRequestURI(), resultJson);
        return result;
    }

    @AfterThrowing(value = "controllerLogService()", throwing="e")
    public void afterThrow(PlatformException e) {
        HttpServletRequest request = getRequest();
        log.error("请求 [{}] 抛出异常 : {}", request.getRequestURI(), e.getMessage());
    }

    private ServletRequestAttributes getSra() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return  (ServletRequestAttributes) ra;
    }

    private HttpServletRequest getRequest() {
        return getSra().getRequest();
    }

    private String getRequestJson(ProceedingJoinPoint joinPoint) {
        MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ms.getParameterNames();
        Map<String, Object> map = new HashMap<>();
        int i = 0;
        for (String paramName : paramNames) {
            map.put(paramName, args[i]);
            i++;
        }
        return JsonUtil.getJsonFromObject(map);
    }

}
