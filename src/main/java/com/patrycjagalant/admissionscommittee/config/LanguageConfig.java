package com.patrycjagalant.admissionscommittee.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Locale;
import java.util.function.BiFunction;

/**
 * A configuration class that implements a {@link WebMvcConfigurer} interface.
 * It is used to customize the default configuration to enable the application's
 * internationalization through locale language switch interface.
 *
 * @author Patrycja Galant
 * @see WebMvcConfigurer
 */

@Configuration
public class LanguageConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


    @Override
    public LocalValidatorFactoryBean getValidator() {

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());
        return localValidatorFactoryBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * A method that allows for an easy modification of one or more parameters,
     * without affecting the remaining ones.<br>
     * Implemented based on a <a href="https://stackoverflow.com/a/68212965/19720323">
     * solution from Stack Overflow</a>
     *
     * @return a bi-function that takes the current URI address,
     * searches for the requested parameter name and replaces its value with
     * the provided one if it exists, or adds it to the query parameters if not.
     */
    @Bean
    public BiFunction<String, String, String> replaceOrAddParam() {
        return (paramName, newValue) -> ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam(paramName, newValue)
                .toUriString();
    }
}
