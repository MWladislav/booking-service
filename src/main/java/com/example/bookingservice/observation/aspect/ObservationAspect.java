package com.example.bookingservice.observation.aspect;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ObservationAspect {

    private final ObservationRegistry registry;
    @Around("@annotation(com.example.bookingservice.observation.WithObservation)")
    public Object wrapWithObservation(ProceedingJoinPoint pjp) throws Throwable {
        // invoking original method wrapped with observation
        return Observation
                .createNotStarted(pjp.getSignature().getName(), this.registry)
                .observeChecked(() -> pjp.proceed());
    }
}
