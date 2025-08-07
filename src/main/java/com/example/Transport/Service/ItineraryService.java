package com.example.Transport.Service;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    ItineraryRepo itineraryRepo;
    StopRepo stopRepo;


    /*public Itinerary createItineraryFromMarkers(List<Stop> stops) {
        try {
            if (stops==null || stops.size()<2) {
                throw new IllegalArgumentException("Two stops are required");
            }

            Stop departure = stops.get(0);
            Stop destination = stops.get(1);

            stopRepo.saveAll(stops);

            Itinerary itinerary=new Itinerary();
            itinerary.setItineraryName(departure.getStopName() + " - " + destination.getStopName());
            itinerary.setDeparture(departure);
            itinerary.setDestination(destination);
            itinerary.setStop(stops);

            return itineraryRepo.save(itinerary);

        } catch (IllegalArgumentException e) {
            System.err.println("Errrror of validation : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Errror of creation : " + e.getMessage());
            return null;
        }
    }*/


    public Itinerary createItineraryFromMarkers(Stop departure, Stop destination) {
        try {
            if (departure==null || destination==null) {
                throw new IllegalArgumentException("Two stops are required");
            }


            stopRepo.save(departure);
            stopRepo.save(destination);


            Itinerary itinerary=new Itinerary();
            itinerary.setItineraryName(departure.getStopName() + " - " + destination.getStopName());
            itinerary.setDeparture(departure);
            itinerary.setDestination(destination);
            itinerary.getStop().add(departure);
            itinerary.getStop().add(destination);


            return itineraryRepo.save(itinerary);

        } catch (IllegalArgumentException e) {
            System.err.println("Errrror of validation : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Errror of creation : " + e.getMessage());
            return null;
        }
    }






}
