package com.example.Transport.Controller;


import com.example.Transport.Entities.Stop;
import com.example.Transport.Repositories.StopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stops")
@RequiredArgsConstructor
public class StopController {

    StopRepo stopRepo;

    @PostMapping("/stop")
    public ResponseEntity<Stop> createStop(@RequestBody Stop stop) {
        Stop savedStop = stopRepo.save(stop);
        return ResponseEntity.ok(savedStop);
    }
}
