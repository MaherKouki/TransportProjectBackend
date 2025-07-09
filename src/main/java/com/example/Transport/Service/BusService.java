package com.example.Transport.Service;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Repositories.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BusService implements IBusService{

    private final BusRepository busRepo;



    @Override
    public Bus addBus(Bus bus) {
        return busRepo.save(bus);
    }

    @Override
    public void deleteBus(Long IdBus) {
        busRepo.deleteById(IdBus);
    }

    @Override
    public Bus getBus(Long IdBus) {
        return busRepo.getById(IdBus);
    }

    @Override
    public List<Bus> getAllBus() {
        return busRepo.findAll();
    }
}
