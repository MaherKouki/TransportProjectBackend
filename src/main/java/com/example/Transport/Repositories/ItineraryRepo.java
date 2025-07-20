package com.example.Transport.Repositories;

import com.example.Transport.Entities.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepo extends JpaRepository<Itinerary, Integer> {
}
