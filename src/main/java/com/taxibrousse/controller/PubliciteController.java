package com.taxibrousse.controller;

import com.taxibrousse.entity.Publicite;
import com.taxibrousse.entity.Societe;
import com.taxibrousse.entity.Voyage;
import com.taxibrousse.service.PaiementService;
import com.taxibrousse.service.PubliciteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/publicites")
public class PubliciteController {

    @Autowired
    private PubliciteService publiciteService;

    @Autowired
    private PaiementService paiementService;

    @GetMapping
    public String listePublicites(
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            Model model) {
        
        List<Publicite> publicites;
        BigDecimal chiffreAffairesTotal;
        String filtreActif = "tout";
        
        // Appliquer le filtre si mois et année sont fournis
        if (mois != null && annee != null) {
            publicites = publiciteService.findByMoisAndAnnee(mois, annee);
            chiffreAffairesTotal = publiciteService.calculerChiffreAffairesMoisAnnee(mois, annee);
            filtreActif = "periode";
        } else {
            publicites = publiciteService.findAll();
            chiffreAffairesTotal = publiciteService.calculerChiffreAffairesTotal();
        }
        
        // Grouper les publicités par voyage puis par société
        Map<Voyage, Map<Societe, List<Publicite>>> publicitesParVoyageEtSociete = publicites.stream()
                .collect(Collectors.groupingBy(
                        Publicite::getVoyage,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                Publicite::getSociete,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));
        
        // Calculer les totaux par société par voyage
        Map<String, BigDecimal> totauxParSocieteParVoyage = new HashMap<>();
        // Calculer les paiements par société (clé = societeId)
        Map<Long, BigDecimal> paiementsParSociete = new HashMap<>();
        // Calculer le total des publicités par société (pour le reste à payer)
        Map<Long, BigDecimal> totalPublicitesParSociete = new HashMap<>();
        
        for (Map.Entry<Voyage, Map<Societe, List<Publicite>>> voyageEntry : publicitesParVoyageEtSociete.entrySet()) {
            Voyage voyage = voyageEntry.getKey();
            for (Map.Entry<Societe, List<Publicite>> societeEntry : voyageEntry.getValue().entrySet()) {
                Societe societe = societeEntry.getKey();
                BigDecimal total = societeEntry.getValue().stream()
                        .map(Publicite::getMontantTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                String key = voyage.getId() + "_" + societe.getId();
                totauxParSocieteParVoyage.put(key, total);
                
                // Accumuler le total des publicités par société
                totalPublicitesParSociete.merge(societe.getId(), total, BigDecimal::add);
                
                // Calculer les paiements pour cette société (une seule fois par société)
                if (!paiementsParSociete.containsKey(societe.getId())) {
                    BigDecimal totalPaye;
                    if (mois != null && annee != null) {
                        totalPaye = paiementService.calculerTotalPayeSocieteMoisAnnee(societe, mois, annee);
                    } else {
                        totalPaye = paiementService.calculerTotalPayeSociete(societe);
                    }
                    paiementsParSociete.put(societe.getId(), totalPaye);
                }
            }
        }
        
        // Calculer le reste à payer par société
        Map<Long, BigDecimal> resteAPayerParSociete = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : totalPublicitesParSociete.entrySet()) {
            Long societeId = entry.getKey();
            BigDecimal totalPub = entry.getValue();
            BigDecimal totalPaye = paiementsParSociete.getOrDefault(societeId, BigDecimal.ZERO);
            resteAPayerParSociete.put(societeId, totalPub.subtract(totalPaye));
        }
        
        // Générer les années disponibles pour le filtre (année courante et 5 années précédentes)
        int anneeActuelle = LocalDate.now().getYear();
        List<Integer> annees = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            annees.add(anneeActuelle - i);
        }
        
        model.addAttribute("publicites", publicites);
        model.addAttribute("publicitesParVoyageEtSociete", publicitesParVoyageEtSociete);
        model.addAttribute("totauxParSocieteParVoyage", totauxParSocieteParVoyage);
        model.addAttribute("paiementsParSociete", paiementsParSociete);
        model.addAttribute("resteAPayerParSociete", resteAPayerParSociete);
        model.addAttribute("chiffreAffairesTotal", chiffreAffairesTotal);
        model.addAttribute("filtreActif", filtreActif);
        model.addAttribute("moisSelectionne", mois);
        model.addAttribute("anneeSelectionnee", annee);
        model.addAttribute("annees", annees);
        
        return "publicites";
    }
}
