package com.taxibrousse.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxibrousse.entity.Voyage;
import com.taxibrousse.repository.VoyageRepository;

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
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            voyages = voyageRepository.findByDateHeureBetween(startOfDay, endOfDay);
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

    /**
     * Filtre les voyages par mois, année, gare de départ et gare d'arrivée
     */
    public List<Voyage> filterVoyagesByMoisAnnee(Integer mois, Integer annee, Integer departId, Integer arriveId) {
        List<Voyage> voyages = voyageRepository.findAll();

        // Filtre par mois et/ou année
        if (mois != null && annee != null) {
            YearMonth yearMonth = YearMonth.of(annee, mois);
            LocalDateTime debut = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime fin = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            voyages = voyageRepository.findByDateHeureBetween(debut, fin);
        } else if (annee != null) {
            LocalDateTime debut = LocalDate.of(annee, 1, 1).atStartOfDay();
            LocalDateTime fin = LocalDate.of(annee, 12, 31).atTime(LocalTime.MAX);
            voyages = voyageRepository.findByDateHeureBetween(debut, fin);
        } else if (mois != null) {
            int currentYear = LocalDate.now().getYear();
            YearMonth yearMonth = YearMonth.of(currentYear, mois);
            LocalDateTime debut = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime fin = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            voyages = voyageRepository.findByDateHeureBetween(debut, fin);
        }

        // Filtre par gare de départ
        if (departId != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getTrajet().getDepart().getId().equals(departId))
                    .collect(Collectors.toList());
        }

        // Filtre par gare d'arrivée
        if (arriveId != null) {
            voyages = voyages.stream()
                    .filter(v -> v.getTrajet().getArrive().getId().equals(arriveId))
                    .collect(Collectors.toList());
        }

        return voyages;
    }

    /**
     * Récupère toutes les années distinctes des voyages
     */
    public List<Integer> getAllAnnees() {
        return voyageRepository.findAll().stream()
                .map(v -> v.getDateHeure().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}