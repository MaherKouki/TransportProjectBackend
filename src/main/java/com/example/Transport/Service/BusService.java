package com.example.Transport.Service;

import com.example.Transport.Entities.Bus;
import com.example.Transport.Entities.Itinerary;
import com.example.Transport.Repositories.BusRepository;
import com.example.Transport.Repositories.ItineraryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BusService implements IBusService{

    private final BusRepository busRepo;
    private final ItineraryRepo itineraryRepo;



    @Override
    public Bus addBus(Bus bus) {
        return busRepo.save(bus);
    }

    @Override
    public void deleteBus(Long IdBus) {


        Bus bus = busRepo.findById(IdBus)
                .orElseThrow(()-> new RuntimeException("Bus not found"));

        for (Itinerary itinerary : bus.getItineraries()) {
            itinerary.getBuses().remove(bus);
            itineraryRepo.save(itinerary);
        }
        busRepo.delete(bus);
    }



    public Bus updateBus(Long idBus, Bus updatedBus) {
        Bus existingBus = busRepo.findById(idBus)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        // Update fields
        existingBus.setMatricule(updatedBus.getMatricule());
        existingBus.setMarque(updatedBus.getMarque());

        // Save and return
        return busRepo.save(existingBus);
    }


    @Override
    public Bus getBus(Long IdBus) {
        return busRepo.getById(IdBus);
    }

    @Override
    public List<Bus> getAllBus() {
        return busRepo.findAll();
    }

    @Transactional
    public void affectItineraryToBus(int idItinerary , Long idBus) {

        Bus bus = busRepo.findById(idBus)
                .orElseThrow(()-> new IllegalArgumentException("Bus not found"));
        Itinerary itinerary = itineraryRepo.findById(idItinerary)
                .orElseThrow(()-> new IllegalArgumentException("Itinerary not found"));

        bus.getItineraries().add(itinerary);
        itinerary.getBuses().add(bus);
        itineraryRepo.save(itinerary);
    }



}
