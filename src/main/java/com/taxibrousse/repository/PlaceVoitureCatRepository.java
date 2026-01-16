package com.taxibrousse.repository;

import com.taxibrousse.entity.PlaceVoitureCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceVoitureCatRepository extends JpaRepository<PlaceVoitureCat, Integer> {
    
    List<PlaceVoitureCat> findByPlaceVoitureId(Integer placeVoitureId);
    
    Optional<PlaceVoitureCat> findByPlaceVoitureIdAndCategorieId(Integer placeVoitureId, Integer categorieId);
}
