package com.example.Transport.Entities;

import com.example.Transport.Config.PointDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"time", "bus_id"})
)
public class BusPosition {

    @EmbeddedId
    private BusPositionId id;

    @JsonDeserialize(using = PointDeserializer.class)
    @Column(columnDefinition = "GEOGRAPHY(Point,4326)")
    private Point position;

    // ✅ Access `time` from the embedded ID, or map it read-only if needed:
    @Column(name = "time", insertable = false, updatable = false)
    private long time;

    private Instant savedAt;

    @JsonIgnore
    @ManyToOne
    @MapsId("busId") // map busId from the embedded key
    @JoinColumn(name = "bus_id")
    private Bus bus;
}
