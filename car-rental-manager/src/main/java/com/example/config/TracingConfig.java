package com.example.config;

import brave.Tracing;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

@Configuration
public class TracingConfig {
    private String template = "LocalIp: [{0}], LocalServiceName: [{1}], SpanId: [{2}], SpanName: [{3}]";

    @Bean
    public SpanHandler spanHandler() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext traceContext, MutableSpan span, Cause cause) {
                String name = MessageFormat.format(template, span.localIp(), span.localServiceName(), span.id(), span.name());
                span.name(name);
                return true;
            }
        };
    }

}
