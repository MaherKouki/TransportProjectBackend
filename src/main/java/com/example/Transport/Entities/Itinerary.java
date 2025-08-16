package com.example.Transport.Entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonIgnore
    @ManyToMany (mappedBy = "itinerary")
    private List<Stop> stop = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Stop departure ;

    @ManyToOne(cascade = CascadeType.ALL)
    private Stop destination;

    @ManyToMany
    private Set<Bus> buses = new HashSet<>();


    //continue
    //private Stop departure;

/*
    public Stop getDeparture() {
        return stop.stream().min(Comparator.comparing(Stop::getOrderIndex)).orElse(null);
    }

    public Stop getDestination() {
        return stop.stream().max(Comparator.comparing(Stop::getOrderIndex)).orElse(null);
    }*/

    /*public String nameOfItinerary(Itinerary itinerary) {
        String departure = itinerary.departure.getStopName();
        String destination = itinerary.destination.getStopName();
        return departure + " " + destination;
    }*/




/*
    private String departName;
    private double departAltitude;
    private double departLongitude;

    private String destinationName;
    private double destinationAltitude;
    private double destinationLongitude;
*/

}
