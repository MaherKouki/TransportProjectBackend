package com.example.Transport.Controller;


import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.StopRepo;
import com.example.Transport.Service.StopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stops")
@RequiredArgsConstructor
public class StopController {

    StopRepo stopRepo;
    StopService stopService;

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



}
