package com.taxibrousse.controller;

import com.taxibrousse.entity.VenteProduit;
import com.taxibrousse.service.VenteProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventes")
public class VenteProduitController {

    @Autowired
    private VenteProduitService venteProduitService;

    /**
     * Récupère toutes les ventes de produits
     */
    @GetMapping
    public List<VenteProduit> getAllVentes() {
        return venteProduitService.getAllVentes();
    }

    /**
     * Récupère les ventes pour un voyage spécifique
     */
    @GetMapping("/voyage/{voyageId}")
    public List<VenteProduit> getVentesByVoyage(@PathVariable Integer voyageId) {
        return venteProduitService.getVentesByVoyage(voyageId);
    }

    /**
     * Calcule le montant total des ventes pour un voyage spécifique
     */
    @GetMapping("/montant/voyage/{voyageId}")
    public ResponseEntity<Map<String, BigDecimal>> getMontantTotalVenteByVoyage(@PathVariable Integer voyageId) {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("montantTotal", venteProduitService.getMontantTotalVenteByVoyage(voyageId));
        return ResponseEntity.ok(result);
    }

    /**
     * Calcule le montant total de toutes les ventes
     */
    @GetMapping("/montant/total")
    public ResponseEntity<Map<String, BigDecimal>> getMontantTotalVente() {
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("montantTotal", venteProduitService.getMontantTotalVente());
        return ResponseEntity.ok(result);
    }

    /**
     * Calcule le montant total des ventes par voyage et le total général
     */
    @GetMapping("/montant/details")
    public ResponseEntity<Map<String, BigDecimal>> getMontantTotalVenteDetails() {
        return ResponseEntity.ok(venteProduitService.getMontantTotalVenteDetails());
    }

    /**
     * Sauvegarde une nouvelle vente de produit
     */
    @PostMapping
    public VenteProduit createVente(@RequestBody VenteProduit venteProduit) {
        return venteProduitService.saveVente(venteProduit);
    }

    /**
     * Supprime une vente de produit
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Integer id) {
        venteProduitService.deleteVente(id);
        return ResponseEntity.noContent().build();
    }
}
