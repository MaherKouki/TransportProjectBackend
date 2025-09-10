package com.example.Transport.Service;

import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.BusRepository;
import com.example.Transport.Repositories.ItineraryRepo;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepo itineraryRepo;
    private final StopRepo stopRepo;
    private final BusRepository busRepository;


    public String cleanText(String input) {
        if (input == null) return null;

        // 1. Normalize and remove diacritics (accents)
        String noAccents = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // 2. Remove non-ASCII characters
        String asciiOnly = noAccents.replaceAll("[^\\p{ASCII}]", "");

        // 3. Trim spaces
        return asciiOnly.trim();
    }


    @Transactional
    public void deleteItinerary(int itineraryId) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        // Remove stops association
        for (Stop stop : itinerary.getStops()) {
            stop.getItineraries().remove(itinerary); // optional if bi-directional
        }
        itinerary.getStops().clear();

        // Remove buses association
        itinerary.getBuses().clear();

        // Delete itinerary
        itineraryRepo.delete(itinerary);
    }



    @Transactional
    public Itinerary createItineraryFromMarkers(Stop departure, Stop destination) {
        if (departure == null || destination == null) {
            throw new IllegalArgumentException("Both departure and destination stops are required.");
        }

        // Save or get stops globally
        Stop savedDeparture = saveOrGetStop(departure);
        Stop savedDestination = saveOrGetStop(destination);

        //Bech netjaneb les problemes mte3 l'accent wl3arbi
        String nameItinerary = savedDeparture.getStopName() + " - " + savedDestination.getStopName();


        // Create itinerary without stops yet
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(cleanText(nameItinerary));
        itinerary.setStartTime(savedDeparture.getArrivalTime());
        itinerary.setDeparture(savedDeparture);
        itinerary.setDestination(savedDestination);

        // Save itinerary first to generate ID
        itinerary = itineraryRepo.save(itinerary);

        // Add stops to itinerary
        List<Stop> stops = new ArrayList<>();
        stops.add(savedDeparture);
        stops.add(savedDestination);
        itinerary.setStops(stops);

        // Synchronize Many-to-Many from stop side
        for (Stop stop : stops) {
            if (stop.getItineraries() == null) stop.setItineraries(new ArrayList<>());
            if (!stop.getItineraries().contains(itinerary)) {
                stop.getItineraries().add(itinerary);
                stopRepo.save(stop);
            }
        }

        return itineraryRepo.save(itinerary);
    }

    @Transactional
    public void addStopsToItinerary(int idItinerary, List<Stop> stops) {
        // Fetch itinerary
        Itinerary itinerary = itineraryRepo.findById(idItinerary)
                .orElseThrow(() -> new IllegalArgumentException("Itinerary not found"));

        if (itinerary.getStops() == null) {
            itinerary.setStops(new ArrayList<>());
        }

        // Determine starting orderIndex
        int orderIndex = itinerary.getStops().stream()
                .map(Stop::getOrderIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(0) + 1;

        for (Stop stop : stops) {
            // Save or get the stop globally (may exist in other itineraries)
            Stop savedStop = saveOrGetStop(stop);

            // Check if this stop already exists in THIS itinerary
            boolean alreadyInItinerary = itinerary.getStops().stream()
                    .anyMatch(s ->
                            s.getLatitude() == savedStop.getLatitude() &&
                                    s.getLongitude() == savedStop.getLongitude() &&
                                    s.getStopName().equals(savedStop.getStopName())
                    );

            if (!alreadyInItinerary) {
                // Set orderIndex
                savedStop.setOrderIndex(orderIndex++);
                stopRepo.save(savedStop);

                // Add stop to itinerary
                itinerary.getStops().add(savedStop);

                // Synchronize Many-to-Many
                if (savedStop.getItineraries() == null) savedStop.setItineraries(new ArrayList<>());
                if (!savedStop.getItineraries().contains(itinerary)) {
                    savedStop.getItineraries().add(itinerary);
                    stopRepo.save(savedStop);
                }
            }
        }

        itineraryRepo.save(itinerary);
    }





    public List<Itinerary> getAllItineraries() {
        return itineraryRepo.findAll();
    }


    public Optional<Itinerary> getItineraryById(int id) {
        return itineraryRepo.findById(id);
    }


    /*@Transactional
    public void deleteItinerary(int id) {
        itineraryRepo.deleteById(id);
    }*/


    private Stop saveOrGetStop(Stop stop) {
        return stopRepo.findByStopNameAndLatitudeAndLongitude(
                stop.getStopName(), stop.getLatitude(), stop.getLongitude()
        ).orElseGet(() -> stopRepo.save(stop));
    }
}
