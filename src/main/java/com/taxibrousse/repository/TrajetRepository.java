package com.taxibrousse.repository;

import com.taxibrousse.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Integer> {
    List<Trajet> findByDepartId(Integer departId);
    List<Trajet> findByArriveId(Integer arriveId);
}
