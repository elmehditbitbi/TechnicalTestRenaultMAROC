package com.renault.digital.TechnicalTest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {
    private static final String VALID_API_KEY = "aedz-151-ftyh-554";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String apiKey = request.getHeader("api-key");
        if (!VALID_API_KEY.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
