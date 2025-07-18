package com.example.Transport.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.HashSet;
import java.util.List;

@Entity
public class Itinerary {


    @Id
    private int idItinerary;

    private String departName;
    private double departAltitude;
    private double departLongitude;



    private String destinationName;
    private double destinationAltitude;
    private double destinationLongitude;


    @OneToMany (mappedBy = "itinerary")
    private List<Stop> stop;
}
