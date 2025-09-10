package com.example.Transport.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.*;

@Entity
@Data
public class Itinerary {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItinerary;


    private String itineraryName;


    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;


    @ManyToOne//(cascade = CascadeType.ALL)
    private Stop departure ;

    @ManyToOne//(cascade = CascadeType.ALL)
    private Stop destination;

    @ManyToMany
    @JoinTable(
            name = "bus_itinerary",
            joinColumns = @JoinColumn(name = "itinerary_id"),
            inverseJoinColumns = @JoinColumn(name = "bus_id")
    )
    private List<Bus> buses = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "stop_itinerary",
            joinColumns = @JoinColumn(name = "itinerary_id"),
            inverseJoinColumns = @JoinColumn(name = "stop_id")
    )
    //@JsonBackReference
    private List<Stop> stops = new ArrayList<>();


}
