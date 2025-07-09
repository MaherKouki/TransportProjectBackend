package com.example.Transport.Controller;


import com.example.Transport.Entities.Bus;
import com.example.Transport.Service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bus")
public class BusController {



    private final BusService busService;
    //BusService busService;

    //http://localhost:8080/bus/position/1
    @PostMapping("/addBus")
    public Bus addMatchs(@RequestBody Bus bus) {
        return busService.addBus(bus);
    }

    @GetMapping( "/getAllBuses")
    public List<Bus> getAllBuses() {
        return busService.getAllBus();
    }

    @GetMapping("/getBusById/{busId}")
    public Bus getBus(@PathVariable("busId") Long busId) {
        return busService.getBus(busId);
    }

    @DeleteMapping("/removeBus/{busId}")
    public void deleteBus(@PathVariable("busId") Long busId) {
        busService.deleteBus(busId);
    }





}
