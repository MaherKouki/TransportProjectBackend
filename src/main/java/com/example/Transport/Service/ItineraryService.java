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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        // Save stops
        Stop savedDeparture = stopRepo.save(departure);
        Stop savedDestination = stopRepo.save(destination);

        // Create itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(savedDeparture.getStopName() + " - " + savedDestination.getStopName());
        itinerary.setStartTime(savedDeparture.getArrivalTime());
        itinerary.setDeparture(savedDeparture);
        itinerary.setDestination(savedDestination);

        // Add stops list (departure + destination)
        List<Stop> stops = new ArrayList<>();
        stops.add(savedDeparture);
        stops.add(savedDestination);
        itinerary.setStop(stops);

        // Save itinerary
        return itineraryRepo.save(itinerary);
    }


    public void addStopsToItinerary(int idItinerary, List<Stop> stops) {

        Itinerary itinerary = itineraryRepo.findById(idItinerary)
                .orElseThrow(() -> new IllegalArgumentException("Itinerary not found"));

        Stop departure = itinerary.getDeparture();
        departure.setOrderIndex(0);
        stopRepo.save(departure);

        Stop destination = itinerary.getDestination();

        List<Stop> existingStops = itinerary.getStop()
                .stream()
                .filter(stop -> ! stop.equals(departure) && ! stop.equals(destination))
                .sorted(Comparator.comparing(stop -> stop.getOrderIndex() !=null ? stop.getOrderIndex() : 0))
                .toList();

        // for theee initial stops li bin destination wldeparture
        int orderIndex = 1;
        for (Stop stop : existingStops) {
            stop.setOrderIndex(orderIndex++);
            stopRepo.save(stop);
        }

        for (Stop stop : stops) {
            stop.setOrderIndex(orderIndex++);
            stopRepo.save(stop);
            itinerary.getStop().add(stop);
        }

        destination.setOrderIndex(orderIndex);
        stopRepo.save(destination);
        itineraryRepo.save(itinerary);
    }















    @Transactional
    public Itinerary createItineraryFromStops(List<Stop> stops) {
        if (stops == null || stops.size() < 2) {
            throw new IllegalArgumentException("At least two stops (departure and destination) are required.");
        }

        // Ensure orderIndex is set properly to avoid DB constraint issues
        for (int i = 0; i < stops.size(); i++) {
            stops.get(i).setOrderIndex(i);
        }

        // Save stops
        List<Stop> savedStops = stopRepo.saveAll(stops);

        // Create itinerary
        Stop departure = savedStops.get(0);
        Stop destination = savedStops.get(savedStops.size() - 1);

        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(departure.getStopName() + " - " + destination.getStopName());
        itinerary.setStartTime(null);
        itinerary.setDeparture(departure);
        itinerary.setDestination(destination);
        itinerary.setStop(new ArrayList<>(savedStops));

        // Save itinerary
        Itinerary savedItinerary = itineraryRepo.save(itinerary);

        // Update relationship in each stop
        for (Stop s : savedStops) {
            s.getItinerary().add(savedItinerary);
        }
        stopRepo.saveAll(savedStops);

        return savedItinerary;
    }

/*
    public Itinerary createItineraryFromMarkers(Stop departure, Stop destination) {

        if (departure == null || destination == null) {
            throw new IllegalArgumentException("Both departure and destination stops are required.");
        }

        // Set order indexes
        departure.setOrderIndex(null);
        destination.setOrderIndex(null);

        // Save stops first
        Stop savedDeparture = stopRepo.save(departure);
        Stop savedDestination = stopRepo.save(destination);

        // Create itinerary
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(savedDeparture.getStopName() + " - " + savedDestination.getStopName());
        itinerary.setStartTime(savedDeparture.getArrivalTime());
        itinerary.setDeparture(savedDeparture);
        itinerary.setDestination(savedDestination);

        // Initialize stop list
        List<Stop> stops = new ArrayList<>();
        stops.add(savedDeparture);
        stops.add(savedDestination);
        itinerary.setStop(stops);

        // Save itinerary
        Itinerary savedItinerary = itineraryRepo.save(itinerary);

        // Update stops' many-to-many relation
        savedDeparture.getItinerary().add(savedItinerary);
        savedDestination.getItinerary().add(savedItinerary);

        stopRepo.save(savedDeparture);
        stopRepo.save(savedDestination);

        return savedItinerary;
    }*/


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