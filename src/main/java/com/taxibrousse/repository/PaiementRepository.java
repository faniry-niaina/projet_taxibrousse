package com.taxibrousse.repository;

import com.taxibrousse.entity.Paiement;
import com.taxibrousse.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {
    
    // Trouver les paiements par société
    List<Paiement> findBySociete(Societe societe);
    
    // Trouver les paiements par date
    List<Paiement> findByDatePaiement(LocalDate datePaiement);
    
    // Trouver les paiements entre deux dates
    List<Paiement> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver les paiements par société et entre deux dates
    List<Paiement> findBySocieteAndDatePaiementBetween(Societe societe, LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver les paiements par mois et année
    @Query("SELECT p FROM Paiement p WHERE MONTH(p.datePaiement) = :mois AND YEAR(p.datePaiement) = :annee")
    List<Paiement> findByMoisAndAnnee(@Param("mois") int mois, @Param("annee") int annee);
    
    // Trouver les paiements par société, mois et année
    @Query("SELECT p FROM Paiement p WHERE p.societe = :societe AND MONTH(p.datePaiement) = :mois AND YEAR(p.datePaiement) = :annee")
    List<Paiement> findBySocieteAndMoisAndAnnee(@Param("societe") Societe societe, @Param("mois") int mois, @Param("annee") int annee);
}
