package com.example.Transport.Repositories;

import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Entities.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StopRepo extends JpaRepository<Stop, Integer> {


    Optional<List<Stop>> findByStopName(String stopName);

    Optional<Stop> findByStopNameAndLatitudeAndLongitude(String stopName, double latitude, double longitude);



    @Query("SELECT DISTINCT s FROM Stop s " +
            "JOIN s.itineraries i " +
            "WHERE LOWER(i.itineraryName) LIKE LOWER(CONCAT('%', :itineraryName, '%'))")
    List<Stop> findStopsByItinerary(@Param("itineraryName") String itineraryName);


}
