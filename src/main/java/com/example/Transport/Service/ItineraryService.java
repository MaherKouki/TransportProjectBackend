package com.example.Transport.Service;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    ItineraryRepo itineraryRepo;

    public Itinerary createItinerary(List<Stop> stops) {
        try {
            if (stops==null || stops.size()<2) {
                throw new IllegalArgumentException("At least two stops are required.");
            }

            Itinerary it = new Itinerary();
            it.setDeparture(stops.get(0));
            it.setDestination(stops.get(1));
            it.setItineraryName(stops.get(0).getStopName() + " - " + stops.get(1).getStopName());

            return itineraryRepo.save(it);

        } catch (IllegalArgumentException e) {
            // Handle missing or insufficient stops
            System.err.println("Input error: " + e.getMessage());
            return null;

        } catch (Exception e) {
            System.err.println("Unexpected error occurred while creating itinerary: " + e.getMessage());
            return null;
        }
    }





}
