package com.ex.sothat.config;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.util.Map;

public class Paths {
    protected static final String[] COMMON_URLS = {
            "/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**",
            "/search/**", "/tackStack/**", "/project/**",
//            "/ymls-dev/env",
            "/", "/**",
    };
    protected static final String[] RESOURCE_URLS = {
            "/css/**", "/js/**", "/images/**",
            "/fonts/**", "/favicon.ico",
            "/thymeleaf"
    };
    protected static final String[] RESOURCE_PATHS = {
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/",
            "classpath:/resources/",
            "classpath:"
    };
    protected static final Map<String, String> PERMITTED_URLS = Map.of(
            "/homePage", "/homePage",
            "/loginPage", "/loginPage",
            "/boardPage", "/boardPage",
            "/joinPage", "/joinPage"
    );
    protected static final Map<String, String> POST_REQUESTS = Map.of(
            "/oauth2/authorization/google", "/oauth2/authorization/google",
            "/", "/homePage"
    );
    protected static void configureViewControllers(ViewControllerRegistry registry) {
        PERMITTED_URLS.forEach((url, viewName) ->
                registry.addViewController(url).setViewName(viewName)
        );
        registry.addViewController("/").setViewName("index");
    }
}