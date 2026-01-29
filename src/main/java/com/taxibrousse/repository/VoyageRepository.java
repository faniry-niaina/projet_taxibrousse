package com.taxibrousse.repository;

import com.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Integer> {
    List<Voyage> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);
    List<Voyage> findByTrajet_Depart_Id(Integer departId);
    List<Voyage> findByTrajet_Arrive_Id(Integer arriveId);
    List<Voyage> findByTrajet_Depart_IdAndTrajet_Arrive_Id(Integer departId, Integer arriveId);
}