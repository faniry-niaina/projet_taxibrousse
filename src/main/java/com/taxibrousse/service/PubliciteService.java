package com.taxibrousse.service;

import com.taxibrousse.entity.Publicite;
import com.taxibrousse.entity.Societe;
import com.taxibrousse.entity.Voyage;
import com.taxibrousse.repository.PubliciteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PubliciteService {

    @Autowired
    private PubliciteRepository publiciteRepository;

    public List<Publicite> findAll() {
        return publiciteRepository.findAll();
    }

    public Optional<Publicite> findById(Integer id) {
        return publiciteRepository.findById(id);
    }

    public Publicite save(Publicite publicite) {
        return publiciteRepository.save(publicite);
    }

    public void deleteById(Integer id) {
        publiciteRepository.deleteById(id);
    }

    public List<Publicite> findBySociete(Societe societe) {
        return publiciteRepository.findBySociete(societe);
    }

    public List<Publicite> findByVoyage(Voyage voyage) {
        return publiciteRepository.findByVoyage(voyage);
    }

    public List<Publicite> findByDate(LocalDate date) {
        return publiciteRepository.findByDate(date);
    }

    public List<Publicite> findByDateBetween(LocalDate dateDebut, LocalDate dateFin) {
        return publiciteRepository.findByDateBetween(dateDebut, dateFin);
    }

    public List<Publicite> findBySocieteAndDateBetween(Societe societe, LocalDate dateDebut, LocalDate dateFin) {
        return publiciteRepository.findBySocieteAndDateBetween(societe, dateDebut, dateFin);
    }

    // Calcul du montant total des publicités pour une période
    public BigDecimal calculerMontantTotalPeriode(LocalDate dateDebut, LocalDate dateFin) {
        List<Publicite> publicites = findByDateBetween(dateDebut, dateFin);
        return publicites.stream()
                .map(Publicite::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du montant total des publicités pour une société et une période
    public BigDecimal calculerMontantTotalParSociete(Societe societe, LocalDate dateDebut, LocalDate dateFin) {
        List<Publicite> publicites = findBySocieteAndDateBetween(societe, dateDebut, dateFin);
        return publicites.stream()
                .map(Publicite::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Trouver les publicités par mois et année
    public List<Publicite> findByMoisAndAnnee(int mois, int annee) {
        return publiciteRepository.findByMoisAndAnnee(mois, annee);
    }

    // Calcul du chiffre d'affaires total de toutes les publicités
    public BigDecimal calculerChiffreAffairesTotal() {
        List<Publicite> publicites = findAll();
        return publicites.stream()
                .map(Publicite::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du chiffre d'affaires pour un mois et une année
    public BigDecimal calculerChiffreAffairesMoisAnnee(int mois, int annee) {
        List<Publicite> publicites = findByMoisAndAnnee(mois, annee);
        return publicites.stream()
                .map(Publicite::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
