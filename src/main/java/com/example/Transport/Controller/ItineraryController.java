package com.example.Transport.Controller;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import com.example.Transport.Service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itinerary")
@RequiredArgsConstructor
public class ItineraryController {


    private final ItineraryService itineraryService;
    private final ItineraryRepo itineraryRepo;



    @PostMapping("/create")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Map<String, Stop> stops) {
        try {
            Stop departure = stops.get("departure");
            Stop destination = stops.get("destination");

            if (departure == null || destination == null) {
                return ResponseEntity.badRequest().build();
            }

            Itinerary itinerary = itineraryService.createItineraryFromMarkers(departure, destination);
            return ResponseEntity.ok(itinerary);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getItineraryByDeparture/{departurePoint}")
    public ResponseEntity<List<Itinerary>> getItinerariesByDeparture(@PathVariable String derpaturePoint) {
        List<Itinerary> itineraries = itineraryRepo.findByDeparture_StopNameContainingIgnoreCase(derpaturePoint).orElseThrow(()->new IllegalArgumentException("No itinerary found with that stop point"));
        return ResponseEntity.ok(itineraries);
    }

    @GetMapping("/getItineraryByDestination/{destinationPoint}")
    public ResponseEntity<List<Itinerary>> getItinerariesByDestination(@PathVariable String destinationPoint) {
        List<Itinerary> itineraries = itineraryRepo.findByDestination_StopNameContainingIgnoreCase(destinationPoint).orElseThrow(()->new IllegalArgumentException("No itinerary found with that stop point"));
        return ResponseEntity.ok(itineraries);
    }


    /*@GetMapping("/getItineraryByDestination/{destinationPoint}")
    public ResponseEntity<List<Itinerary>> getItinerariesByDestination(@PathVariable String destinationPoint) {
        List<Itinerary> itineraries = itineraryRepo.findByDestination_StopNameContainingIgnoreCase(destinationPoint).orElseThrow(()->new IllegalArgumentException("No itinerary found with that stop point"));
        return ResponseEntity.ok(itineraries);
    }*/








}

/*
import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.StopRepo;
import com.example.Transport.Service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itinerary")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final StopRepo stopRepo;

    // Create itinerary directly from Stop objects
    @PostMapping("/create")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Map<String, Stop> request) {
        try {
            Stop departure = request.get("departure");
            Stop destination = request.get("destination");

            if (departure == null || destination == null) {
                return ResponseEntity.badRequest().build();
            }

            Itinerary itinerary = itineraryService.createItineraryFromMarkers(departure, destination);
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create individual stop
    @PostMapping("/stops")
    public ResponseEntity<Stop> createStop(@RequestBody Stop stop) {
        try {
            Stop savedStop = stopRepo.save(stop);
            return ResponseEntity.ok(savedStop);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all itineraries
    @GetMapping("/all")
    public ResponseEntity<List<Itinerary>> getAllItineraries() {
        try {
            List<Itinerary> itineraries = itineraryService.getAllItineraries();
            return ResponseEntity.ok(itineraries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}*/