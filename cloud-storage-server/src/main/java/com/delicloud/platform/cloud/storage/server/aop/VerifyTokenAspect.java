package com.delicloud.platform.cloud.storage.server.aop;

import com.delicloud.platform.cloud.storage.server.entity.TUserInfo;
import com.delicloud.platform.cloud.storage.server.repository.cache.UserTokenCache;
import com.delicloud.platform.common.lang.exception.PlatformException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class VerifyTokenAspect {

    @Autowired
    UserTokenCache userTokenCache;

    // 定义切点Pointcut
    @Pointcut("execution(* *(..)) && @annotation(Token)")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String token = request.getHeader("token");
        TUserInfo tUserInfo = userTokenCache.getUserToken(token);
        if (tUserInfo == null) {
            throw new PlatformException("登录token错误");
        }

        MethodSignature msg = (MethodSignature)joinPoint.getSignature();
        String[] paramName = msg.getParameterNames();
        List<String> paramNameList = Arrays.asList(paramName);
        Object[] args = joinPoint.getArgs();
        if (!paramNameList.contains("account")) {
            return joinPoint.proceed(args);
        }
        //返回参数位置
        Integer pos = paramNameList.indexOf("account");
        args[pos] = tUserInfo.getAccount();
        return joinPoint.proceed(args);


    }
}
