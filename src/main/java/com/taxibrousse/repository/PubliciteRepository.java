package com.taxibrousse.repository;

import com.taxibrousse.entity.Publicite;
import com.taxibrousse.entity.Societe;
import com.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PubliciteRepository extends JpaRepository<Publicite, Integer> {
    
    // Trouver les publicités par société
    List<Publicite> findBySociete(Societe societe);
    
    // Trouver les publicités par voyage
    List<Publicite> findByVoyage(Voyage voyage);
    
    // Trouver les publicités par date
    List<Publicite> findByDate(LocalDate date);
    
    // Trouver les publicités entre deux dates
    List<Publicite> findByDateBetween(LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver les publicités par société et entre deux dates
    List<Publicite> findBySocieteAndDateBetween(Societe societe, LocalDate dateDebut, LocalDate dateFin);
    
    // Trouver les publicités par voyage et société
    List<Publicite> findByVoyageAndSociete(Voyage voyage, Societe societe);
    
    // Trouver les publicités par mois et année
    @Query("SELECT p FROM Publicite p WHERE MONTH(p.date) = :mois AND YEAR(p.date) = :annee")
    List<Publicite> findByMoisAndAnnee(@Param("mois") int mois, @Param("annee") int annee);
}
