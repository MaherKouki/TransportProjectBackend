package com.example.Transport.Service;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Entities.BusPositionId;
import com.example.Transport.Repositories.BusPositionRepository;
import com.example.Transport.Repositories.BusRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusPositionService {

    private final BusPositionRepository busPosRepo;
    private final BusService busService;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final BusRepository busRepo;
    private final BusPositionRepository busPositionRepository;


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
    public BusPosition getLatestPosition(Long busId) {
        return busPositionRepository.findByBusIdOrderByIdTimeDesc(busId)
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

