package com.targil.calendar.common.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/*
    Not for production use.
    Bucket4j is preferable for complex applications.
    but for the home assigment - it is can be enough :)
 */
@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {
    private final RateLimitingService rateLimitingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = request.getRemoteAddr();
        if (rateLimitingService.isLimitExceeded(clientIp)) {
            throw new RateLimitException("Too many requests");
        }
        return true;
    }
}
