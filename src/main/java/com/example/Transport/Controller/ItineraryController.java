package com.example.Transport.Controller;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itinerary")
@RequiredArgsConstructor
public class ItineraryController {


    ItineraryService itineraryService;


    @PostMapping("/create")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Stop departure, @RequestBody Stop destination) {
        Itinerary itinerary = itineraryService.createItineraryFromMarkers(departure, destination);
        return ResponseEntity.ok(itinerary);
    }

}
