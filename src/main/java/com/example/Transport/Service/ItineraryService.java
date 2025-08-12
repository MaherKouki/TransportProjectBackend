package com.example.Transport.Service;

/*
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

}*/


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepo itineraryRepo;
    private final StopRepo stopRepo;

    @Transactional
    public Itinerary createItineraryFromMarkers(Stop departure, Stop destination) {
        if (departure == null || destination == null) {
            throw new IllegalArgumentException("Both departure and destination stops are required.");
        }

        // Remove the unique constraint issue by setting unique orderIndex per itinerary
        // For now, let's remove orderIndex from the individual stops since it causes conflicts
        departure.setOrderIndex(0);
        destination.setOrderIndex(1);

        // Save stops first
        Stop savedDeparture = stopRepo.save(departure);
        Stop savedDestination = stopRepo.save(destination);

        // Create itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(savedDeparture.getStopName() + " - " + savedDestination.getStopName());
        itinerary.setStartTime(null);
        itinerary.setDeparture(savedDeparture);
        itinerary.setDestination(savedDestination);

        // Initialize the stop list if null
        if (itinerary.getStop() == null) {
            itinerary.setStop(new ArrayList<>());
        }

        // Add stops to the itinerary
        itinerary.getStop().add(savedDeparture);
        itinerary.getStop().add(savedDestination);

        // Save and return the itinerary
        Itinerary savedItinerary = itineraryRepo.save(itinerary);

        // Update the many-to-many relationship from the other side
        savedDeparture.getItinerary().add(savedItinerary);
        savedDestination.getItinerary().add(savedItinerary);

        stopRepo.save(savedDeparture);
        stopRepo.save(savedDestination);

        return savedItinerary;
    }

    public List<Itinerary> getAllItineraries() {
        return itineraryRepo.findAll();
    }

    public Optional<Itinerary> getItineraryById(int id) {
        return itineraryRepo.findById(id);
    }

    @Transactional
    public Itinerary addStopToItinerary(int itineraryId, Stop stop) {
        Optional<Itinerary> itineraryOpt = itineraryRepo.findById(itineraryId);

        if (itineraryOpt.isEmpty()) {
            throw new IllegalArgumentException("Itinerary not found with id: " + itineraryId);
        }

        Itinerary itinerary = itineraryOpt.get();

        // Set the order index for the new stop
        int nextOrderIndex = itinerary.getStop().size();
        stop.setOrderIndex(nextOrderIndex);

        // Save the stop first
        Stop savedStop = stopRepo.save(stop);

        // Add to itinerary
        itinerary.getStop().add(savedStop);

        // Update the many-to-many relationship
        savedStop.getItinerary().add(itinerary);

        // Save both entities
        stopRepo.save(savedStop);
        return itineraryRepo.save(itinerary);
    }

    @Transactional
    public void deleteItinerary(int id) {
        itineraryRepo.deleteById(id);
    }
}