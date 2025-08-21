package com.example.Transport.Controller;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Entities.BusPositionId;
import com.example.Transport.Repositories.BusPositionRepository;
import com.example.Transport.Repositories.BusRepository;
import com.example.Transport.Service.BusPositionService;
import com.example.Transport.Service.BusService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Position;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/busPosition")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // autorise uniquement Angular local
public class BusPositionController {

    private final BusPositionService busPositionService;
    private final BusPositionRepository busPosRepo;
    private final BusService busService;

    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final BusRepository busRepository;
    private final BusPositionRepository busPositionRepository;



    @PostMapping("/locationn")
    public ResponseEntity<String> receiveLocationn(@RequestBody Map<String, Object> payload) {
        try {
            Long busId = ((Number) payload.get("busId")).longValue();
            Double latitude = ((Number) payload.get("lat")).doubleValue();
            Double longitude = ((Number) payload.get("lon")).doubleValue();
            Long timestamp = payload.get("time") != null ? ((Number) payload.get("time")).longValue() : null;

            busPositionService.saveBusPosition(busId, latitude, longitude, timestamp);
            return ResponseEntity.ok("Location received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid payload");
        }
    }




    /*@PostMapping("/savePosition")
    public BusPosition saveBusPosition(
            @RequestParam Long busId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam long time
    ) {
        return busPositionService.savePosition(busId, latitude, longitude, time);
    }*/




    //http://localhost:8080/bus/position/2
    @PostMapping("/position/{busId}")
    public ResponseEntity<?> updatePosition(@PathVariable Long busId,
                                            @RequestBody BusPosition busPosition) {
        try {
            long time = busPosition.getTimestamp(); // from deserialized JSON

            BusPositionId id = new BusPositionId(busId, time);
            if (busPosRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Position already exists");
            }
            busPosition.setId(id);
            busPosition.setBus(busService.getBus(busId));
            busPosition.setSavedAt(Instant.now());

            busPosRepo.save(busPosition);
            return ResponseEntity.status(HttpStatus.CREATED).body("Position saved successssfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + ex.getMessage());
        }
    }





    /*@GetMapping("/lastPosition/{idBus}")
    public ResponseEntity<?> getLastPosition(@PathVariable Long idBus) {

        if (!busPosRepo.existsById_BusId(idBus)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Bus " + idBus + " does not exits ");
        }
        BusPosition position = busPosRepo.findLastPosition(idBus).get(0);
        return ResponseEntity.ok(position);
    }*/

    @GetMapping("/lastPosition/{idBus}")
    public ResponseEntity<Object> getLastPosition(@PathVariable Long idBus) {
        if (!busPosRepo.existsById_BusId(idBus)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bus " + idBus + " does not exit ");
        }
        BusPosition position = busPosRepo.findTopByBus_IdBusOrderById_TimeDesc(idBus);
        return ResponseEntity.ok(position);
    }


    @GetMapping( "/getPositionPerBus/{busId}")
    public List<BusPosition> getPositionPerBus(@PathVariable Long busId) {
        return busPosRepo.busPositionsForBus(busId);
    }




    //todo: receive the location from the phone

    @PostMapping("/location")
    public ResponseEntity<String> receiveLocation(@RequestBody Map<String, Object> payload) {
        try {
            // Extract fields safely
            Long busId = ((Number) payload.get("busId")).longValue();
            Double latitude = ((Number) payload.get("lat")).doubleValue();
            Double longitude = ((Number) payload.get("lon")).doubleValue();
            Long timestamp = payload.get("time") != null ? ((Number) payload.get("time")).longValue() : null;

            // Save in DB
            busPositionService.saveBusPosition(busId, latitude, longitude, timestamp);

            return ResponseEntity.ok("Location received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid payload");
        }
    }

    // Update or add a new bus position manually
    @PostMapping("/update")
    public ResponseEntity<BusPosition> updateLocation(
            @RequestParam Long busId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) Long timestamp
    ) {
        try {
            BusPosition position = busPositionService.saveBusPosition(busId, latitude, longitude, timestamp);
            return ResponseEntity.ok(position);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Get latest position of a bus
    @GetMapping("/latest/{busId}")
    public ResponseEntity<BusPosition> getLatestLocation(@PathVariable Long busId) {
        BusPosition position = busPositionService.getLatestPosition(busId);
        if (position == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(position);
    }






    // Allow any device
    @PostMapping("/owntracks")
    public ResponseEntity<String> receiveOwnTracksLocation(@RequestBody Map<String, Object> payload) {
        try {
            // OwnTracks sends coordinates as "lat", "lon", timestamp as "tst"
            Double latitude = ((Number) payload.get("lat")).doubleValue();
            Double longitude = ((Number) payload.get("lon")).doubleValue();

            // timestamp in OwnTracks is in seconds, convert to milliseconds
            Long timestamp = payload.get("tst") != null
                    ? ((Number) payload.get("tst")).longValue() * 1000
                    : Instant.now().toEpochMilli();

            // You can hardcode busId for testing, or map devices to busIds
            Long busId = 1L;

            // Save position in DB
            busPositionService.saveBusPosition(busId, latitude, longitude, timestamp);

            return ResponseEntity.ok("OwnTracks location received successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid OwnTracks payload");
        }
    }















    /*@PostMapping("/location")
    public ResponseEntity<String> receiveLocation(@RequestBody Map<String, Object> payload) {
        System.out.println("Location received: " + payload);

        // Example: extract fields
        Double lat = (Double) payload.get("lat");
        Double lon = (Double) payload.get("lon");

        // Save in DB or compare with PC location
        // locationRepository.save(new LocationEntity(lat, lon));

        return ResponseEntity.ok("Location received");
    }





    @PostMapping("/update")
    public ResponseEntity<BusPosition> updateLocation(
            @RequestParam Long busId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(required = false) Long timestamp
    ) {
        try {
            BusPosition position = busPositionService.saveBusPosition(busId, latitude, longitude, timestamp);
            return ResponseEntity.ok(position);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint to get the latest location of a bus
    @GetMapping("/latest/{busId}")
    public ResponseEntity<BusPosition> getLatestLocation(@PathVariable Long busId) {
        BusPosition position = busPositionService.getLatestPosition(busId);
        if (position == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(position);
    }*/






}

