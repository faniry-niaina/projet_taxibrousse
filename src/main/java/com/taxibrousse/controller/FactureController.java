package com.taxibrousse.controller;

import com.taxibrousse.entity.Paiement;
import com.taxibrousse.entity.Societe;
import com.taxibrousse.service.PaiementService;
import com.taxibrousse.service.SocieteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/factures")
public class FactureController {

    @Autowired
    private SocieteService societeService;

    @Autowired
    private PaiementService paiementService;

    @GetMapping
    public String listeFactures(Model model) {
        model.addAttribute("societes", societeService.findAll());
        return "factures";
    }

    @GetMapping("/societe/{id}")
    public String detailFactureSociete(@PathVariable Long id, Model model) {
        Optional<Societe> societeOpt = societeService.findById(id);
        if (societeOpt.isEmpty()) {
            return "redirect:/factures";
        }
        Societe societe = societeOpt.get();
        model.addAttribute("societe", societe);
        model.addAttribute("paiements", paiementService.findBySociete(societe));
        return "factureDetail";
    }

    @PostMapping("/paiement")
    public String insererPaiement(@RequestParam Long societeId,
                                   @RequestParam String datePaiement,
                                   @RequestParam BigDecimal montant,
                                   RedirectAttributes redirectAttributes) {
        Optional<Societe> societeOpt = societeService.findById(societeId);
        if (societeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Société introuvable");
            return "redirect:/factures";
        }

        Societe societe = societeOpt.get();
        
        // Vérifier que le montant ne dépasse pas le reste à payer
        BigDecimal resteAPayer = societe.getResteAPayer();
        if (montant.compareTo(resteAPayer) > 0) {
            redirectAttributes.addFlashAttribute("error", 
                "Le montant du paiement (" + montant + " Ar) dépasse le reste à payer (" + resteAPayer + " Ar)");
            return "redirect:/factures/societe/" + societeId;
        }

        Paiement paiement = new Paiement();
        paiement.setSociete(societe);
        paiement.setMontantPaye(montant);
        paiement.setDatePaiement(LocalDate.parse(datePaiement));
        
        paiementService.save(paiement);
        
        redirectAttributes.addFlashAttribute("message", 
            "Paiement de " + montant + " Ar enregistré avec succès pour " + societe.getLib());
        
        return "redirect:/factures/societe/" + societeId;
    }

    @PostMapping("/paiement/supprimer/{id}")
    public String supprimerPaiement(@PathVariable Integer id, 
                                     @RequestParam Long societeId,
                                     RedirectAttributes redirectAttributes) {
        paiementService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Paiement supprimé avec succès");
        return "redirect:/factures/societe/" + societeId;
    }
}
