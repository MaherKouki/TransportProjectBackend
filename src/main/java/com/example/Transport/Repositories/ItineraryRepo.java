package com.example.Transport.Repositories;

import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItineraryRepo extends JpaRepository<Itinerary, Integer> {

    @Query("""
    SELECT i from Itinerary i
    where i.departure.stopName = :stopPoint
    OR i.destination.stopName = :stopPoint
    """)
    Optional<List<Itinerary>> findStopByDepartureOrDestination(@Param("stopPoint") String stopPoint);


    List<Itinerary> findDistinctByStops_StopNameContainingIgnoreCase(String stopName);





    Optional<List<Itinerary>> findByDeparture_StopNameContainingIgnoreCase(String nameStop);

    Optional<List<Itinerary>> findByDestination_StopNameContainingIgnoreCase(String nameStop);



}
