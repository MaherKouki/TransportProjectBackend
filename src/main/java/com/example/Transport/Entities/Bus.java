package com.example.Transport.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Bus {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idBus;

        @Column(unique = true, nullable = false)
        private String matricule;

        private String marque;

        @JsonIgnore
        /*@OneToMany (mappedBy = "bus")
        Set<BusPosition> busPosition = new HashSet<>();*/

        @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<BusPosition> busPosition;




        @ManyToMany(mappedBy = "buses")
        @JsonIgnore
        private List<Itinerary> itineraries = new ArrayList<>();
}
