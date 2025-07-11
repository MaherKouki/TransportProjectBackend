package com.example.Transport.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Data
public class BusPositionId {


    private Long busId;
    private long time;

    public BusPositionId() {}

    public BusPositionId(Long busId, long time) {
        this.busId = busId;
        this.time = time;
    }

}
