package com.example.Transport.Service;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    ItineraryRepo itineraryRepo;
    StopRepo stopRepo;

    @Transactional
    public Itinerary createItineraryFromMarkers(Stop departure, Stop destination) {
        if (departure == null || destination == null) {
            throw new IllegalArgumentException("Both departure and destination stops are required.");
        }

        // Save stops first
        Stop savedDeparture = stopRepo.save(departure);
        Stop savedDestination = stopRepo.save(destination);

        // Create itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(savedDeparture.getStopName() + " - " + savedDestination.getStopName());
        itinerary.setDeparture(savedDeparture);
        itinerary.setDestination(savedDestination);

        // Ensure list is initialized
        if (itinerary.getStop() == null) {
            itinerary.setStop(new ArrayList<>());
        }
        itinerary.getStop().add(savedDeparture);
        itinerary.getStop().add(savedDestination);

        return itineraryRepo.save(itinerary);
    }





}
