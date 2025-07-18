package com.example.Transport.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity

public class Stop {


    @Id
    private int id;

    private String stopName;

    private double latitude;

    private double longitude;

    @ManyToOne
    private Itinerary itinerary;





}
