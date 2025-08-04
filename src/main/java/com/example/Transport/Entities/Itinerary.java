package com.example.Transport.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Entity
@Data
public class Itinerary {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItinerary;


    private String itineraryName;
    private LocalTime startTime;

    @ManyToMany (mappedBy = "itinerary")
    private List<Stop> stop = new ArrayList<>();

    @ManyToOne
    private Stop departure ;

    @ManyToOne
    private Stop destination;


    //continue
    //private Stop departure;

/*
    public Stop getDeparture() {
        return stop.stream().min(Comparator.comparing(Stop::getOrderIndex)).orElse(null);
    }

    public Stop getDestination() {
        return stop.stream().max(Comparator.comparing(Stop::getOrderIndex)).orElse(null);
    }*/

    public String nameOfItinerary(Itinerary itinerary) {
        String departure = itinerary.departure.getStopName();
        String destination = itinerary.destination.getStopName();
        return departure + " " + destination;
    }

/*
    private String departName;
    private double departAltitude;
    private double departLongitude;

    private String destinationName;
    private double destinationAltitude;
    private double destinationLongitude;
*/

}
