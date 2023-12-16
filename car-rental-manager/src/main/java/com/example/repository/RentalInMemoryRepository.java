package com.example.repository;

import com.example.metric.GaugeRegistry;
import com.example.repository.entity.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class RentalInMemoryRepository implements RentalRepository {

    @Autowired
    private GaugeRegistry gaugeRegistry;
    private Map<String, Rent> storage = new HashMap<>();
    @Autowired
    public RentalInMemoryRepository(GaugeRegistry gaugeRegistry) {
        gaugeRegistry.gaugeMap("Gauge:RentalInMemoryRepository.storage.size", storage);
    }

    @Override
    public Rent save(Rent rent) {
        if (rent == null) {
            throw new IllegalArgumentException("Unable to save null rent");
        }
        if (rent.getId() == null) {
            rent.setId(generateId());
        }
        this.storage.put(rent.getId(), rent);
        return rent;
    }

    @Override
    public boolean isCarInRent(String carId, LocalDateTime from, LocalDateTime to) {
        return this.storage.values().stream()
                .filter(rent -> rent.getCarId().equals(carId))
                .anyMatch(rent -> {
                    if (rent.getFrom().equals(from) && rent.getTo().equals(to)) {
                        return true;
                    }
                    if (rent.getFrom().isAfter(from) && rent.getFrom().isBefore(to)) {
                        return true;
                    }
                    if (rent.getTo().isAfter(from) && rent.getTo().isBefore(to)) {
                        return true;
                    }
                    return false;
                });
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

}
