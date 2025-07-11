package com.example.Transport.Controller;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Entities.BusPositionId;
import com.example.Transport.Repositories.BusPositionRepository;
import com.example.Transport.Service.BusPositionService;
import com.example.Transport.Service.BusService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Position;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/busPosition")
@RequiredArgsConstructor
public class BusPositionController {

    private final BusPositionService positionService;
    private final BusPositionRepository busPosRepo;
    private final BusService busService;



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
            return ResponseEntity.status(HttpStatus.CREATED).body("Position saved successfully");
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

}

