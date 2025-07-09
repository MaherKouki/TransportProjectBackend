package com.example.Transport.Controller;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Entities.BusPositionId;
import com.example.Transport.Repositories.BusPositionRepository;
import com.example.Transport.Service.BusPositionService;
import com.example.Transport.Service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/busPosition")
@RequiredArgsConstructor
public class BusPositionController {

    private final BusPositionService positionService;
    private final BusPositionRepository busPosRepo;
    private final BusService busService;


    //http://localhost:8080/bus/position/1
    /*@PostMapping("/position/{busId}")
    public ResponseEntity<?> updatePosition(@PathVariable Long busId,
                                            @RequestBody BusPosition busPosition) {
        try {
            long time = busPosition.getTime();
            BusPositionId id = new BusPositionId(busId, time);

            if (busPosRepo.existsById(id) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Position already exists for busId=" + busId + " and time=" + time);
            }

            busPosition.setId(id);
            busPosition.setBus(busService.getBus(busId));
            busPosition.setSavedAt(Instant.now());

            busPosRepo.save(busPosition);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + ex.getMessage());
        }
    }*/



    @PostMapping("/position/{busId}")
    public ResponseEntity<?> updatePosition(@PathVariable Long busId,
                                            @RequestBody BusPosition busPosition) {
        try {
            long time = busPosition.getTime(); // retrieved from body

            // Build composite ID
            BusPositionId id = new BusPositionId(busId, time);

            // Check if the entry already exists (prevent duplicates)
            if (busPosRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Position already exists for busId = " + busId + " and time = " + time);
            }

            // Set composite ID
            busPosition.setId(id);
            busPosition.setBus(busService.getBus(busId));
            busPosition.setSavedAt(Instant.now());
            busPosRepo.save(busPosition);

            return ResponseEntity.status(HttpStatus.CREATED).body("Position saved successfully");

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + ex.getMessage());
        }
    }








//        return repository.findByEmail(userEmail)
//                .orElseThrow(() -> new UsernameNotFoundException("User Email not found"));
//    }

    @GetMapping( "/getPositionPerBus/{busId}")
    public List<BusPosition> getPositionPerBus(@PathVariable Long busId) {
        return busPosRepo.busPositionsForBus(busId);
    }

}

