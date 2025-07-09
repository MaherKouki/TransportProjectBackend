package com.example.Transport.Service;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Repositories.BusPositionRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusPositionService {

    private final BusPositionRepository busPosRepo;
    private final BusService busService;
    private final GeometryFactory geometryFactory = new GeometryFactory();



    public List<BusPosition> getPositionsByBusId(Long busId) {
        return busPosRepo.busPositionsForBus(busId);
    }


    public void savePosition(Long busId, double longitude, double latitude, long time) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        BusPosition position = new BusPosition();
        //position.setBusId(busId);
        position.setPosition(point);
        position.setSavedAt(Instant.now());

        position.setTime(time);

        position.setBus(busService.getBus(busId));
        busPosRepo.save(position);
    }
}

