package com.taxibrousse.controller;

import com.taxibrousse.entity.Voyage;
import com.taxibrousse.service.ReservationService;
import com.taxibrousse.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private VoyageService voyageService;

    @GetMapping("/{voyageId}")
    public String formReservation(@PathVariable Integer voyageId, Model model, RedirectAttributes redirectAttributes) {
        Voyage voyage = voyageService.getVoyageById(voyageId);
        if (voyage == null) {
            redirectAttributes.addFlashAttribute("error", "Voyage introuvable");
            return "redirect:/";
        }
        if (voyage.getPlacesRestantes() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Désolé, il n'y a plus de places disponibles pour ce voyage !");
            return "redirect:/";
        }
        model.addAttribute("voyage", voyage);
        model.addAttribute("typePlaces", reservationService.getAllTypePlaces());
        model.addAttribute("categories", reservationService.getAllCategories());
        return "reservationForm";
    }

    @PostMapping("/{voyageId}")
    public String reserver(@PathVariable Integer voyageId,
                           @RequestParam String nomClient,
                           @RequestParam(value = "typePlaceId[]", required = false) Integer[] typePlaceIds,
                           @RequestParam(value = "categorieId[]", required = false) Integer[] categorieIds,
                           @RequestParam(value = "nbPlaces[]", required = false) Integer[] nbPlacesArray,
                           RedirectAttributes redirectAttributes) {

        if (nomClient == null || nomClient.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez entrer votre nom");
            return "redirect:/reservation/" + voyageId;
        }

        if (typePlaceIds == null || typePlaceIds.length == 0) {
            redirectAttributes.addFlashAttribute("error", "Veuillez ajouter au moins une ligne de réservation");
            return "redirect:/reservation/" + voyageId;
        }

        List<ReservationService.LigneReservation> lignes = new ArrayList<>();
        for (int i = 0; i < typePlaceIds.length; i++) {
            if (typePlaceIds[i] != null && categorieIds[i] != null && nbPlacesArray[i] != null && nbPlacesArray[i] > 0) {
                lignes.add(new ReservationService.LigneReservation(typePlaceIds[i], categorieIds[i], nbPlacesArray[i]));
            }
        }

        if (lignes.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez ajouter au moins une ligne de réservation valide");
            return "redirect:/reservation/" + voyageId;
        }

        String erreur = reservationService.reserverMultiple(voyageId, nomClient, lignes);
        if (erreur != null) {
            redirectAttributes.addFlashAttribute("error", erreur);
            return "redirect:/reservation/" + voyageId;
        }

        redirectAttributes.addFlashAttribute("message", "Merci " + nomClient + " pour votre confiance ! " + lignes.size() + " réservation(s) effectuée(s).");
        return "redirect:/";
    }
}