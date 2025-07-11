package com.example.Transport.Entities;

import com.example.Transport.Config.BusPositionDeserializer;
import com.example.Transport.Config.PointDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        indexes = {
               @Index(name = "idx_column_time",  columnList = "time")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"time", "bus_id"})
)
@JsonDeserialize(using = BusPositionDeserializer.class)
public class BusPosition {

    @EmbeddedId
    @JsonIgnore
    private BusPositionId id;

    //@JsonDeserialize(using = PointDeserializer.class)
    @JsonIgnore
    @Column(columnDefinition = "GEOGRAPHY(Point,4326)")
    private Point position;


    @JsonProperty("latitude")
    public double getLatitude() {
        return this.position.getY(); // Y = latitude
    }

    @JsonProperty("longitude")
    public double getLongitude() {
        return this.position.getX(); // X = longitude
    }

    @JsonProperty("busId")
    public Long getBusId() {
        return id != null ? id.getBusId() : null;
    }

    @JsonProperty("time")
    public Long getTimestamp() {
        return id != null ? id.getTime() : null;
    }


    //Access `time` from the embedded ID, or map it read-only if needed:
    //@JsonIgnore
    @Column(name = "time", insertable = false, updatable = false)
    private long time;

    private Instant savedAt;

    @JsonIgnore
    @ManyToOne
    @MapsId("busId") // map busId from the embedded key
    @JoinColumn(name = "bus_id")
    private Bus bus;
}
