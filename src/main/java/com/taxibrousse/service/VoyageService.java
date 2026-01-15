package com.taxibrousse.service;

import com.taxibrousse.entity.Voyage;
import com.taxibrousse.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoyageService {

    @Autowired
    private VoyageRepository voyageRepository;

    public List<Voyage> getAllVoyages() {
        return voyageRepository.findAll();
    }

    public Voyage getVoyageById(Integer id) {
        return voyageRepository.findById(id).orElse(null);
    }

    public List<Voyage> filterVoyages(LocalDate date, Integer departId, Integer arriveId) {
        List<Voyage> voyages;
        
        if (date != null) {
            voyages = voyageRepository.findByDateHeure(date);
        } else {
            voyages = voyageRepository.findAll();
        }

        if (departId != null) {
            voyages.removeIf(v -> !v.getTrajet().getDepart().getId().equals(departId));
        }

        if (arriveId != null) {
            voyages.removeIf(v -> !v.getTrajet().getArrive().getId().equals(arriveId));
        }

        return voyages;
    }
}