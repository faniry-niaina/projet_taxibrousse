package com.taxibrousse.repository;

import com.taxibrousse.entity.TypePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypePlaceRepository extends JpaRepository<TypePlace, Integer> {
    Optional<TypePlace> findByLibelle(String libelle);
}
