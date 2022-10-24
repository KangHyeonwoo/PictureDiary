package com.picture.diary.config;

import com.picture.diary.common.annotation.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

    /**
     *  Message Properties Setting
     *
     *  @see https://derveljunit.tistory.com/338
     */
    @Bean
    public LocaleResolver defaultLocaleResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREAN);

        log.info("LocaleResolver Bean Created.");
        return localeResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        //제공하지 않는 언어로 요청왔을 때의 처리
        Locale.setDefault(Locale.KOREAN);

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages/messages");
        messageSource.setDefaultEncoding(Encoding.DEFAULT_CHARSET.toString());
        messageSource.setDefaultLocale(Locale.getDefault());
        messageSource.setCacheSeconds(600);

        log.info("messageSource Bean Created. Default Charset is {} and Default Locale is {}",
                Encoding.DEFAULT_CHARSET.toString(), Locale.getDefault());

        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(@Autowired ReloadableResourceBundleMessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }
}
