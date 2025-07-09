package com.example.Transport.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class BusPositionId {


    private Long busId;
    private long time;

    public BusPositionId() {}

    public BusPositionId(Long busId, long time) {
        this.busId = busId;
        this.time = time;
    }

    // equals() and hashCode() required
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusPositionId)) return false;
        BusPositionId that = (BusPositionId) o;
        return time == that.time && Objects.equals(busId, that.busId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(busId, time);
    }

    // Getters and setters
}
