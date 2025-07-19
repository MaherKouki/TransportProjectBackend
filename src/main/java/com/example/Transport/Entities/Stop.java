package com.example.Transport.Entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Stop {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStop;

    private String stopName;
    private double latitude;
    private double longitude;

    private int orderIndex;

    @ManyToOne
    private Itinerary itinerary;





}
