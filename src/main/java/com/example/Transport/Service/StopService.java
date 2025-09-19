package com.example.Transport.Service;


import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.ItineraryRepo;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StopService {

    public final StopRepo stopRepo;
    public final ItineraryRepo itineraryRepo;

    //public final ItineraryRepo itineraryRepo;


    public List<Stop> saveStops(List<Stop> stops){
        AtomicInteger index = new AtomicInteger(0);

        return stops.stream()
                .map(original -> {
                    Stop stop = new Stop();
                    stop.setStopName(original.getStopName());
                    stop.setLatitude(original.getLatitude());
                    stop.setLongitude(original.getLongitude());
                    stop.setOrderIndex(index.getAndIncrement());
                    //todo
                    stop.setArrivalTime(original.getArrivalTime());
                    //todo
                    stop.setEstimatedTime(original.getEstimatedTime());
                    return stopRepo.save(stop);
                }) .collect(Collectors.toList());

    }



    /*public void deleteStop(int stopId) {
        Stop stop = stopRepo.findById(stopId).
                orElseThrow(() -> new RuntimeException("Stop not found"));
        if (stop.getItineraries() != null) {
            for(Itinerary itinerary : stop.getItineraries()) {
                itinerary.getStops().remove(stop);
                itineraryRepo.save(itinerary);
            }
        }
        stopRepo.delete(stop);

    }*/


    @Transactional
    public void deleteStop(int stopId) {
        Stop stop = stopRepo.findById(stopId)
                .orElseThrow(() -> new RuntimeException("Stop not found"));

        // 1. Check if stop is used as departure
        boolean isDeparture = itineraryRepo.existsByDeparture(stop);

        // 2. Check if stop is used as destination
        boolean isDestination = itineraryRepo.existsByDestination(stop);

        if (isDeparture || isDestination) {
            throw new RuntimeException("Stop is used as a departure or destination and cannot be deleted");
        }

        // 3. If it's only in many-to-many (intermediate stops), remove relationship
        for (Itinerary itinerary : stop.getItineraries()) {
            itinerary.getStops().remove(stop);  // remove link
        }

        // 4. Now delete the stop
        stopRepo.delete(stop);
    }

    public Stop updateStop(int id, Stop stopDetails) {
        Stop stop = stopRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stop not found"));

        stop.setStopName(stopDetails.getStopName());
        stop.setArrivalTime(stopDetails.getArrivalTime());
        stop.setEstimatedTime(stopDetails.getEstimatedTime());
        stop.setLatitude(stopDetails.getLatitude());
        stop.setLongitude(stopDetails.getLongitude());
        stop.setOrderIndex(stopDetails.getOrderIndex());

        return stopRepo.save(stop);
    }

}





        /*int index = 1;
        List<Stop> savedStops = new ArrayList<Stop>();

        for (Stop stop : stops){
            Stop st = new Stop();
            st.setStopName(stop.getStopName());
            st.setLatitude(stop.getLatitude());
            st.setLongitude(stop.getLongitude());
            st.setOrderIndex(index ++);
            //todo
            st.setArrivalTime(stop.getArrivalTime());
            //todo
            st.setEstimatedTime(stop.getEstimatedTime());

            savedStops.add(stopRepo.save(st));
        }
        return savedStops;*/

