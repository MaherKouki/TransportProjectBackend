package com.example.Transport.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Data
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStop;

    private String stopName;
    private double latitude;
    private double longitude;

    //Updatable hedhi
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime estimatedTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;

    //@Column(unique = true)
    private Integer orderIndex;

    /*@ManyToOne
    private Itinerary itinerary;*/

    //@JsonIgnore
    @JsonIgnore
    @ManyToMany(mappedBy = "stops")
    private List<Itinerary> itineraries = new ArrayList<>();


}
