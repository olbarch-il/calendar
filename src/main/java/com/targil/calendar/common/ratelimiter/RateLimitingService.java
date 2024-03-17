package com.targil.calendar.common.ratelimiter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Not for production use.
    Bucket4j is preferable for complex applications.
    but for the home assigment - it is can be enough :)
 */
@Service
public class RateLimitingService {
    private final ConcurrentHashMap<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    @Value("${rate.limit.max-requests-per-minute:50}")
    private int MAX_REQUESTS_PER_MINUTE;

    public boolean isLimitExceeded(String ipAddress) {
        AtomicInteger requests = requestCountsPerIpAddress.computeIfAbsent(ipAddress, k -> new AtomicInteger(0));
        if (requests.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            return true;
        }
        requestCountsPerIpAddress.entrySet().removeIf(entry -> entry.getValue().get() > MAX_REQUESTS_PER_MINUTE);
        return false;
    }
}
