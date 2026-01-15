package com.taxibrousse.controller;

import com.taxibrousse.entity.Voyage;
import com.taxibrousse.service.ReservationService;
import com.taxibrousse.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "reservationForm";
    }

    @PostMapping("/{voyageId}")
    public String reserver(@PathVariable Integer voyageId,
                           @RequestParam String nomClient,
                           @RequestParam int nbPlaces,
                           @RequestParam(required = false) Integer typePlaceId,
                           RedirectAttributes redirectAttributes) {

        String erreur = reservationService.reserver(voyageId, nomClient, nbPlaces, typePlaceId);
        if (erreur != null) {
            redirectAttributes.addFlashAttribute("error", erreur);
            return "redirect:/reservation/" + voyageId;
        }

        redirectAttributes.addFlashAttribute("message", "Merci " + nomClient + " pour votre confiance !");
        return "redirect:/";
    }
}