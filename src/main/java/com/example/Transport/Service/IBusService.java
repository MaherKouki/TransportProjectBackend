package com.example.Transport.Service;

import com.example.Transport.Entities.Bus;

import java.util.List;

public interface IBusService {

    Bus addBus(Bus bus);
    void deleteBus(Long IdBus);
    Bus getBus(Long IdBus);
    List<Bus> getAllBus();


}
