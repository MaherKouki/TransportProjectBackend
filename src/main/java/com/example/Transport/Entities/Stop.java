package com.example.Transport.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
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
    private LocalTime estimatedTime;
    private LocalTime arrivalTime;

    @Column(unique = true)
    private int orderIndex;

    /*@ManyToOne
    private Itinerary itinerary;*/


    @JsonIgnore
    @ManyToMany
    private Set<Itinerary> itinerary;


}
