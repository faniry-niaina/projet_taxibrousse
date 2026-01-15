package com.taxibrousse.service;

import com.taxibrousse.entity.Gare;
import com.taxibrousse.repository.GareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GareService {

    @Autowired
    private GareRepository gareRepository;

    // Récupérer toutes les gares
    public List<Gare> getAllGares() {
        return gareRepository.findAll();
    }

    // Récupérer une gare par son ID
    public Gare getGareById(Integer id) {
        Optional<Gare> gare = gareRepository.findById(id);
        return gare.orElse(null);
    }

    // Ajouter ou modifier une gare
    public Gare saveGare(Gare gare) {
        return gareRepository.save(gare);
    }

    // Supprimer une gare
    public void deleteGare(Integer id) {
        gareRepository.deleteById(id);
    }
}