package com.example.Transport.Entities;


import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Entity
public class Itinerary {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItinerary;


    private String itineraryName;
    private LocalTime startTime;


/*
    private String departName;
    private double departAltitude;
    private double departLongitude;

    private String destinationName;
    private double destinationAltitude;
    private double destinationLongitude;
*/



}
