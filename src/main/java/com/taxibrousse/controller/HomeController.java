package com.taxibrousse.controller;

import com.taxibrousse.entity.Voyage;
import com.taxibrousse.service.GareService;
import com.taxibrousse.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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