package com.test.mqserver.config;

import com.test.mqserver.exception.CustomException;
import com.test.mqserver.service.MessageService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    public SpringContextProvider() {
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static CustomException createPlatformException(int code, Object... args) {
        MessageService messageService = (MessageService)getApplicationContext().getBean(MessageService.class);
        String message = messageService.getMessage(code, args);
        return new CustomException(code, message);
    }
}