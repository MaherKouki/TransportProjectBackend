package com.example.Transport.Repositories;

import com.example.Transport.Entities.BusPosition;
import com.example.Transport.Entities.BusPositionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BusPositionRepository extends JpaRepository<BusPosition, BusPositionId> {



    boolean existsById_BusId(Long busId);


    @Query("SELECT b FROM BusPosition b WHERE b.bus.idBus= :idBus")
    List<BusPosition> busPositionsForBus(@Param("idBus") Long idBus);


    @Query("""
    SELECT b 
    FROM BusPosition b 
    WHERE b.bus.idBus = :idBus 
    ORDER BY b.time DESC
""")
    List<BusPosition> findLastPosition(@Param("idBus") Long idBus);


    BusPosition findTopByBus_IdBusOrderById_TimeDesc(Long idBus);







}
