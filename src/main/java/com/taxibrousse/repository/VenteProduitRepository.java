package com.taxibrousse.repository;

import com.taxibrousse.entity.VenteProduit;
import com.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VenteProduitRepository extends JpaRepository<VenteProduit, Integer> {
    
    List<VenteProduit> findByVoyage(Voyage voyage);
    
    List<VenteProduit> findByVoyageId(Integer voyageId);
    
    @Query("SELECT SUM(vp.nombre * vp.produit.prix) FROM VenteProduit vp WHERE vp.voyage.id = :voyageId")
    BigDecimal getMontantTotalVenteByVoyage(@Param("voyageId") Integer voyageId);
    
    @Query("SELECT SUM(vp.nombre * vp.produit.prix) FROM VenteProduit vp")
    BigDecimal getMontantTotalVente();
}
