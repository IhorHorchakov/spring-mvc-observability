package com.example.config;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationHandler;
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

    @Bean
    public SpanHandler spanHandler() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
                // Here we can expand span name with more info
                return true;
            }
        };
    }

    /*
        Spring Actuator injects DefaultMeterObservationHandler by default and no need to declare this bean.
        We create this bean intentionally to have a fixed place to debug the creation of handler.
    */
    @Bean
    public ObservationHandler observationHandler(MeterRegistry meterRegistry) {
        return new DefaultMeterObservationHandler(meterRegistry);
    }

    /* Declare the ObservedAspect bean needed to support @Observed annotation */
    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
