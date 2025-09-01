package com.example.Transport.Service;

import com.example.Transport.Entities.*;
import com.example.Transport.Repositories.BusPositionRepository;
import com.example.Transport.Repositories.BusRepository;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusPositionService {

    private final BusPositionRepository busPosRepo;
    private final BusService busService;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final BusRepository busRepo;
    private final BusPositionRepository busPositionRepository;
    private final StopRepo stopRepo;


    public List<BusPosition> getPositionsByBusId(Long busId) {
        return busPosRepo.busPositionsForBus(busId);
    }

    /*public void savePosition(Long busId, double longitude, double latitude, long time) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        BusPosition position = new BusPosition();
        //position.setBusId(busId);
        position.setPosition(point);
        position.setSavedAt(Instant.now());

        position.setTime(time);

        position.setBus(busService.getBus(busId));
        busPosRepo.save(position);
    }*/


    /*public Stop nearestStop(double latitude, double longitude, Stop destination){
        if (destination == null || destination.getItinerary() == null) return null;

        return destination.getItinerary().stream()
                .filter(it -> it.getStop() != null)
                .flatMap(it -> it.getStop().stream())
                .min(Comparator.comparingDouble(stop -> haversine(latitude, longitude, stop.getLatitude(), stop.getLongitude())))
                .orElse(null);
    }*/

    public Duration timeToNearestStop(double latitude, double longitude, int destinationId) {
        Stop nearest = nearestStop(latitude, longitude, destinationId);

        if (nearest == null)
            return Duration.ZERO;

        // 1. Distance in kilometers → convert to meters
        double distanceKm = haversine(latitude, longitude, nearest.getLatitude(), nearest.getLongitude());
        double distanceMeters = distanceKm * 1000;

        // 2. Average walking speed (1.4 m/s ≈ 5 km/h)
        double walkingSpeed = 1.4;

        // 3. Travel time in seconds
        long travelSeconds = (long) (distanceMeters / walkingSpeed);

        return Duration.ofSeconds(travelSeconds);
    }




    public Stop nearestStop(double latitude, double longitude, int destinationId) {
        // 1️⃣ Fetch destination Stop from DB
        Stop destination = stopRepo.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Stop not found"));

        // 2️⃣ Check if the Stop or its itineraries exist
        if (destination.getItineraries() == null || destination.getItineraries().isEmpty()) {
            return null;
        }

        // 3️⃣ Find nearest stop using Haversine
        return destination.getItineraries().stream()
                .filter(itinerary -> itinerary.getStops() != null && !itinerary.getStops().isEmpty())
                .flatMap(it -> it.getStops().stream())
                .min(Comparator.comparingDouble(stop -> haversine(latitude, longitude, stop.getLatitude(), stop.getLongitude())))
                .orElse(null);
    }



    /*public Stop nearestStop(double latitude, double longitude, Stop destination) {
        if (destination == null || destination.getItinerary() == null) {
            return null; // pas d'itinéraire disponible
        }

        List<Itinerary> itineraries = destination.getItinerary();
        Stop nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Itinerary itinerary : itineraries) {
            if (itinerary.getStop() == null) continue; // éviter NullPointerException
            for (Stop stop : itinerary.getStop()) {
                double distance = haversine(latitude, longitude, stop.getLatitude(), stop.getLongitude());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = stop;
                }
            }
        }

        return nearest;
    }*/

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // rayon de la Terre en mètres
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // distance en mètres
    }






    //private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public BusPosition savePosition(Long busId, double latitude, double longitude, long time) {
        Bus bus = busRepo.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        BusPositionId id = new BusPositionId(busId, time);

        BusPosition busPosition = new BusPosition();
        busPosition.setId(id);
        busPosition.setBus(bus);
        busPosition.setPosition(point);
        busPosition.setTime(time);
        busPosition.setSavedAt(Instant.now());

        return busPositionRepository.save(busPosition);
    }



    @Transactional
    public BusPosition saveBusPosition(Long busId, double latitude, double longitude, Long timestamp) {
        Bus bus = busRepo.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found"));

        BusPosition position = new BusPosition();
        position.setBus(bus);
        position.setPosition(geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(longitude, latitude)));

        long time = timestamp != null ? timestamp : Instant.now().toEpochMilli();

        // Set embedded ID for composite key
        position.setId(new com.example.Transport.Entities.BusPositionId(busId, time));
        position.setSavedAt(Instant.now());

        return busPositionRepository.save(position);
    }

    // Get latest position of a bus
    /*public BusPosition getLatestPosition(Long busId) {
        return busPositionRepository.findByBusIdOrderByIdTimeDesc(busId)
                .stream()
                .findFirst()
                .orElse(null);
    }*/


    public BusPosition getLatestPosition(Long busId) {
        return busPositionRepository.findByIdBusIdOrderByIdTimeDesc(busId)
                .stream()
                .findFirst()
                .orElse(null);
    }

















        /*@Transactional
    public BusPosition saveBusPosition(Map<String, Object> payload) {
        Double lat = (Double) payload.get("lat");
        Double lon = (Double) payload.get("lon");
        String busName = payload.get("bus") != null ? payload.get("bus").toString() : "default";

        BusPosition position = new BusPosition();
        position.setPositi;
        position.setLatitude(lat);
        position.setLongitude(lon);
        position.setTimestamp(Instant.now());
        position.setBusName(busName);

        return repository.save(position);
    }*/


    /*@Transactional
    public BusPosition saveBusPosition(Long busId, double latitude, double longitude, Long timestamp) {
        Bus bus = busRepo.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found"));

        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        BusPositionId id = new BusPositionId(busId, timestamp != null ? timestamp : Instant.now().toEpochMilli());

        BusPosition busPosition = new BusPosition();
        busPosition.setId(id);
        busPosition.setBus(bus);
        busPosition.setPosition(point);
        busPosition.setSavedAt(Instant.now());

        return busPositionRepository.save(busPosition);
    }

    public BusPosition getLatestPosition(Long busId) {
        List<BusPosition> positions = busPositionRepository.findTop1ByIdBusIdOrderByIdTimeDesc(busId);
        return positions.isEmpty() ? null : positions.get(0);
    }*/





}

