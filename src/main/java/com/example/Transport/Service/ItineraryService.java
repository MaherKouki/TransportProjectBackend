package com.example.Transport.Service;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    ItineraryRepo itineraryRepo;





    public Itinerary createItinerary(List<Stop> stops) {

        Itinerary it = new Itinerary();

        it.setDeparture(stops.get(0));
        it.setDestination(stops.get(1));
        it.setItineraryName(stops.get(0).getStopName() + " " + stops.get(1).getStopName());

        itineraryRepo.save(it);
        return it;

    }




}
