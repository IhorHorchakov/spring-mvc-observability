package com.example.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GaugeRegistry {
    @Autowired
    private MeterRegistry meterRegistry;

    public <K,V> void gaugeMap(String metricName, Map<K,V> map) {
        Gauge
                .builder(metricName, map, Map::size)
                .register(meterRegistry);
    }
}
