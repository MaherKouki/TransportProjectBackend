package com.example.Transport.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;


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
    private LocalTime estimatedTime;

    private LocalTime arrivalTime;



    @Column(unique = true)
    private int orderIndex;

    @ManyToOne
    private Itinerary itinerary;





}
