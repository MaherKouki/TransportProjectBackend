package com.example.Transport.Repositories;

import com.example.Transport.Entities.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusRepository extends JpaRepository<Bus , Long> {
}
