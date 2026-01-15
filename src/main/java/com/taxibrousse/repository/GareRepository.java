package com.taxibrousse.repository;

import com.taxibrousse.entity.Gare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GareRepository extends JpaRepository<Gare, Integer> {
}