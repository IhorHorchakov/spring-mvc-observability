package com.example.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    /*
        Spring Actuator injects SimpleMeterRegistry by default and no need to declare this bean manually.
        We create this bean intentionally to have a fixed place to debug the creation of registry.
    */
    @Bean
    public MeterRegistry meterRegistry() {
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        return meterRegistry;
    }
//
//    /* Declare the ObservedAspect bean needed to use @Observed annotation */
//    @Bean
//    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
//        return new ObservedAspect(observationRegistry);
//    }
}
