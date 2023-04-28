package br.arruda.autenticador.service.message.impl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import br.arruda.autenticador.service.message.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final String DEFAULT_MESSAGE = "No such message foung in i18N file";

    @NonNull private final MessageSource messages;

    @Override
    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String obj = null;
        return getMessage(code, locale, obj);
    }

    private String getMessage(String code, Locale locale, String... args) {
        return messages.getMessage(code, args, DEFAULT_MESSAGE, locale);
    }

}