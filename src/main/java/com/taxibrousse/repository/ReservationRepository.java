package com.taxibrousse.repository;

import com.taxibrousse.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByVoyageId(Integer voyageId);
    List<Reservation> findByTypePlaceId(Integer typePlaceId);
}