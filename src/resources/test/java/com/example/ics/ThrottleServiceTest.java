package com.example.ics;

import com.example.ics.service.ThrottleService;
import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ThrottleServiceTest {
    private ThrottleService throttleService;

    @Mock
    private RateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        throttleService = new ThrottleService();
        throttleService.rateLimiter = rateLimiter;
    }

    @Test
    void testShouldThrottle_whenAcquireSucceeds() {
        when(rateLimiter.tryAcquire()).thenReturn(true);

        boolean result = throttleService.shouldThrottle();

        assertFalse(result);
    }

    @Test
    void testShouldThrottle_whenAcquireFails() {
        when(rateLimiter.tryAcquire()).thenReturn(false);

        boolean result = throttleService.shouldThrottle();

        assertTrue(result);
    }
}
