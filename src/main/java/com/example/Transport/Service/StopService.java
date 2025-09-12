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



    public void deleteStop(int stopId) {
        Stop stop = stopRepo.findById(stopId).
                orElseThrow(() -> new RuntimeException("Stop not found"));
        if (stop.getItineraries() != null) {
            for(Itinerary itinerary : stop.getItineraries()) {
                itinerary.getStops().remove(stop);
                itineraryRepo.save(itinerary);
            }
        }
        stopRepo.delete(stop);

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

