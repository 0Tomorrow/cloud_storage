package com.test.mqserver.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Service
public class MessageService {
    @Autowired
    private MessageSource messageSource;

    public MessageService() {
    }

    @PostConstruct
    public void init() {
        if (this.messageSource instanceof AbstractResourceBasedMessageSource) {
            ((AbstractResourceBasedMessageSource)this.messageSource).setBasenames(new String[]{"error", "messages"});
        }

    }

    public String getMessage(int error, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();

        try {
            return this.messageSource.getMessage(String.valueOf(error), args, locale);
        } catch (Exception var5) {
            return "";
        }
    }
}
