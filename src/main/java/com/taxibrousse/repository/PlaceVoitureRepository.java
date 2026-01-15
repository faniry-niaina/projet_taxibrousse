package com.taxibrousse.repository;

import com.taxibrousse.entity.PlaceVoiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceVoitureRepository extends JpaRepository<PlaceVoiture, Integer> {
    List<PlaceVoiture> findByVoitureId(Integer voitureId);
    List<PlaceVoiture> findByTypePlaceId(Integer typePlaceId);
}
