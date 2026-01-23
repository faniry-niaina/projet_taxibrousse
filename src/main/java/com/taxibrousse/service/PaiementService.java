package com.taxibrousse.service;

import com.taxibrousse.entity.Paiement;
import com.taxibrousse.entity.Societe;
import com.taxibrousse.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> findById(Integer id) {
        return paiementRepository.findById(id);
    }

    public Paiement save(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public void deleteById(Integer id) {
        paiementRepository.deleteById(id);
    }

    public List<Paiement> findBySociete(Societe societe) {
        return paiementRepository.findBySociete(societe);
    }

    public List<Paiement> findByMoisAndAnnee(int mois, int annee) {
        return paiementRepository.findByMoisAndAnnee(mois, annee);
    }

    public List<Paiement> findBySocieteAndMoisAndAnnee(Societe societe, int mois, int annee) {
        return paiementRepository.findBySocieteAndMoisAndAnnee(societe, mois, annee);
    }

    // Calcul du total des paiements pour une société (tous les paiements)
    public BigDecimal calculerTotalPayeSociete(Societe societe) {
        List<Paiement> paiements = findBySociete(societe);
        return paiements.stream()
                .map(Paiement::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du total des paiements pour une société sur un mois/année
    public BigDecimal calculerTotalPayeSocieteMoisAnnee(Societe societe, int mois, int annee) {
        List<Paiement> paiements = findBySocieteAndMoisAndAnnee(societe, mois, annee);
        return paiements.stream()
                .map(Paiement::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du total de tous les paiements
    public BigDecimal calculerTotalPaiements() {
        List<Paiement> paiements = findAll();
        return paiements.stream()
                .map(Paiement::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du total des paiements pour un mois/année
    public BigDecimal calculerTotalPaiementsMoisAnnee(int mois, int annee) {
        List<Paiement> paiements = findByMoisAndAnnee(mois, annee);
        return paiements.stream()
                .map(Paiement::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
