package com.taxibrousse.service;

import com.taxibrousse.entity.VenteProduit;
import com.taxibrousse.entity.Voyage;
import com.taxibrousse.repository.VenteProduitRepository;
import com.taxibrousse.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VenteProduitService {

    @Autowired
    private VenteProduitRepository venteProduitRepository;

    @Autowired
    private VoyageRepository voyageRepository;

    /**
     * Récupère toutes les ventes de produits
     */
    public List<VenteProduit> getAllVentes() {
        return venteProduitRepository.findAll();
    }

    /**
     * Récupère les ventes pour un voyage spécifique
     */
    public List<VenteProduit> getVentesByVoyage(Integer voyageId) {
        return venteProduitRepository.findByVoyageId(voyageId);
    }

    /**
     * Calcule le montant total des ventes pour un voyage spécifique
     */
    public BigDecimal getMontantTotalVenteByVoyage(Integer voyageId) {
        BigDecimal montant = venteProduitRepository.getMontantTotalVenteByVoyage(voyageId);
        return montant != null ? montant : BigDecimal.ZERO;
    }

    /**
     * Calcule le montant total de toutes les ventes
     */
    public BigDecimal getMontantTotalVente() {
        BigDecimal montant = venteProduitRepository.getMontantTotalVente();
        return montant != null ? montant : BigDecimal.ZERO;
    }

    /**
     * Calcule le montant total des ventes par voyage et le total général
     * @return Map contenant les montants par voyage (clé = voyageId) et le total général (clé = "total")
     */
    public Map<String, BigDecimal> getMontantTotalVenteDetails() {
        Map<String, BigDecimal> result = new HashMap<>();
        
        // Récupérer tous les voyages
        List<Voyage> voyages = voyageRepository.findAll();
        
        BigDecimal totalGeneral = BigDecimal.ZERO;
        
        for (Voyage voyage : voyages) {
            BigDecimal montantVoyage = getMontantTotalVenteByVoyage(voyage.getId());
            if (montantVoyage.compareTo(BigDecimal.ZERO) > 0) {
                result.put("voyage_" + voyage.getId(), montantVoyage);
                totalGeneral = totalGeneral.add(montantVoyage);
            }
        }
        
        result.put("total", totalGeneral);
        
        return result;
    }

    /**
     * Sauvegarde une vente de produit
     */
    public VenteProduit saveVente(VenteProduit venteProduit) {
        return venteProduitRepository.save(venteProduit);
    }

    /**
     * Supprime une vente de produit
     */
    public void deleteVente(Integer id) {
        venteProduitRepository.deleteById(id);
    }
}
