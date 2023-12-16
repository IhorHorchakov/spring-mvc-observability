package com.example.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {

    /*
        Spring Actuator injects SimpleMeterRegistry by default and no need to declare this bean.
        We create this bean intentionally to have a fixed place to debug the creation of registry.
    */
    @Bean
    public MeterRegistry meterRegistry() {
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        return meterRegistry;
    }

    /* Declare the TimedAspect bean needed to use @Timed annotation */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    /* Declare the CountedAspect bean needed to use @Counted annotation */
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }

}
