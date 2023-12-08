package com.example.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component("randomHealthIndicator")
public class RandomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        double chance = ThreadLocalRandom.current().nextDouble();
        Health.Builder health = Health.up();
        if (chance > 0.9) {
            health = Health.down();
        }
        return health
                .withDetail("chance", chance)
                .build();
    }
}