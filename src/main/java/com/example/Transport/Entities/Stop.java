package com.example.Transport.Entities;

import jakarta.persistence.ManyToOne;

public class Stop {


    private int id;

    private String stopName;

    private double latitude;

    private double longitude;

    @ManyToOne
    private Itinerary itinerary;





}
