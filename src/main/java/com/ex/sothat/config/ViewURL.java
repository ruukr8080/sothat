package com.ex.sothat.config;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.util.Map;

public class ViewURL {
    public static final String[] COMMON_URLS ={
            "/swagger-ui/**","/env",
            "/v3/api-docs/**","/api/auth/**","/search/**",
            "/tackStack/**","/project/**","/","/**"
            ,"/thymeleaf"
    };
    public static final Map<String, String> PERMITTED = Map.of(
            "/homePage", "/homePage",
            "/loginPage", "/loginPage",
            "/boardPage", "/boardPage",
            "/joinPage", "/joinPage"
    );
    public static final Map<String, String> POST_REQUEST = Map.of(
            "/oauth2/authorization/google", "/oauth2/authorization/google",
            "/", "/homePage"
    );




    public static void configureViewControllers(ViewControllerRegistry registry) {
        for (String url : COMMON_URLS) {
            String viewName = getViewName(url);
            registry.addViewController(url).setViewName(viewName);
        }
        PERMITTED.forEach((url, viewName) ->
                registry.addViewController(url).setViewName(viewName)
        );
    }
    private static String getViewName(String url) {
        String viewName = url.replaceAll("[/*]", "").toLowerCase();
        if (viewName.isEmpty()) {
            viewName = "index";
        }
        return viewName;
    }
}
