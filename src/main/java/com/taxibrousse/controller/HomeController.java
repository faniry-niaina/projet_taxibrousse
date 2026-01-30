package com.taxibrousse.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taxibrousse.entity.Voyage;
import com.taxibrousse.service.GareService;
import com.taxibrousse.service.VoyageService;

@Controller
public class HomeController {

    @Autowired
    private VoyageService voyageService;

    @Autowired
    private GareService gareService;

    @GetMapping("/")
    public String accueil(Model model) {
        model.addAttribute("voyages", voyageService.getAllVoyages());
        model.addAttribute("gares", gareService.getAllGares());
        return "accueil";
    }

    @GetMapping("/chiffre-affaire")
    public String chiffreAffaire(
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Integer departId,
            @RequestParam(required = false) Integer arriveId,
            Model model) {
        
        List<Voyage> voyages;
        if (mois != null || annee != null || departId != null || arriveId != null) {
            voyages = voyageService.filterVoyagesByMoisAnnee(mois, annee, departId, arriveId);
        } else {
            voyages = voyageService.getAllVoyages();
        }
        
        model.addAttribute("voyages", voyages);
        model.addAttribute("gares", gareService.getAllGares());
        model.addAttribute("annees", voyageService.getAllAnnees());
        model.addAttribute("moisSelectionne", mois);
        model.addAttribute("anneeSelectionnee", annee);
        model.addAttribute("departIdSelectionne", departId);
        model.addAttribute("arriveIdSelectionne", arriveId);
        return "chiffreAffaire";
    }

    @PostMapping("/chiffre-affaire/filtrer")
    public String filtrerChiffreAffaire(@RequestParam(required = false) String date,
                          @RequestParam(required = false) Integer departId,
                          @RequestParam(required = false) Integer arriveId,
                          Model model) {
        LocalDate localDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
        List<Voyage> voyages = voyageService.filterVoyages(localDate, departId, arriveId);
        model.addAttribute("voyages", voyages);
        model.addAttribute("gares", gareService.getAllGares());
        model.addAttribute("annees", voyageService.getAllAnnees());
        return "chiffreAffaire";
    }

    @PostMapping("/filtrer")
    public String filtrer(@RequestParam(required = false) String date,
                          @RequestParam(required = false) Integer departId,
                          @RequestParam(required = false) Integer arriveId,
                          Model model) {
        LocalDate localDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
        List<Voyage> voyages = voyageService.filterVoyages(localDate, departId, arriveId);
        model.addAttribute("voyages", voyages);
        model.addAttribute("gares", gareService.getAllGares());
        return "accueil";
    }
}