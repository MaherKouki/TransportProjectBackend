package com.example.Transport.Repositories;

import com.example.Transport.Entities.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StopRepo extends JpaRepository<Stop, Integer> {


    Optional<List<Stop>> findByStopName(String stopName);

    Optional<Stop> findByStopNameAndLatitudeAndLongitude(String stopName, double latitude, double longitude);




}
