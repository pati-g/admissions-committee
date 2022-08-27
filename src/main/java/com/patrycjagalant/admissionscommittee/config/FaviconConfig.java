package com.patrycjagalant.admissionscommittee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Collections;

/**
 * A configuration class that enables switching the application's default favicon
 * to the custom one, provided that it is stored in the application's
 * {@code resources/static} folder and has a name {@code favicon.ico}.
 *
 * @author Patrycja Galant
 * @see <a href="https://en.wikipedia.org/wiki/Favicon">Favicon - Wikipedia</a>
 */

@Configuration
public class FaviconConfig {

    @Bean
    public SimpleUrlHandlerMapping customFaviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap(
                "/static/favicon.ico", faviconRequestHandler()));
        return mapping;
    }

    @Bean
    protected ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler
                = new ResourceHttpRequestHandler();
        requestHandler.setLocations(Collections.singletonList(new ClassPathResource("/")));
        return requestHandler;
    }

}
