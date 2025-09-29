package com.example.Transport.Controller;


import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.StopRepo;
import com.example.Transport.Service.StopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stops")
@RequiredArgsConstructor
public class StopController {

    private final StopRepo stopRepo;
    private final StopService stopService;

    @PostMapping("/stop")
    public ResponseEntity<Stop> createStop(@RequestBody Stop stop) {
        Stop savedStop = stopRepo.save(stop);
        return ResponseEntity.ok(savedStop);
    }

    @DeleteMapping("/deleteStop/{id}")
    public ResponseEntity<String> deleteStop(@PathVariable int id) {
        try {
            stopService.deleteStop(id);
            return ResponseEntity.ok("Stop deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/allStops")
    public List<Stop> getAllStops() {
        return stopRepo.findAll();
    }


    /*@PutMapping("/updateStop/{id}")
    public ResponseEntity<Stop> updateStop(@PathVariable int id, @RequestBody Stop stopDetails) {
        return ResponseEntity.ok(stopService.updateStop(id, stopDetails));
    }*/


    @PutMapping("/updateStop/{id}")
    public ResponseEntity<?> updateStop(
            @PathVariable int id,
            @RequestBody Stop stopRequest
    ) {
        Stop stop = stopRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stop not found"));

        // Update only the fields you want
        stop.setStopName(stopRequest.getStopName());
        stop.setOrderIndex(stopRequest.getOrderIndex());
        stop.setEstimatedTime(stopRequest.getEstimatedTime());

        stopRepo.save(stop);
        return ResponseEntity.ok(stop);
    }


    @GetMapping("/getStopById/{id}")
    public ResponseEntity<Stop> getStopById(@PathVariable int id) {
        Stop stop = stopRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Stop not found with id: " + id));
        return ResponseEntity.ok(stop);
    }



}
