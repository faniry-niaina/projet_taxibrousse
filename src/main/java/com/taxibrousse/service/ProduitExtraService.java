package com.taxibrousse.service;

import com.taxibrousse.entity.ProduitExtra;
import com.taxibrousse.repository.ProduitExtraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitExtraService {

    @Autowired
    private ProduitExtraRepository produitExtraRepository;

    /**
     * Récupère tous les produits extra
     */
    public List<ProduitExtra> getAllProduits() {
        return produitExtraRepository.findAll();
    }

    /**
     * Récupère un produit par son ID
     */
    public Optional<ProduitExtra> getProduitById(Integer id) {
        return produitExtraRepository.findById(id);
    }

    /**
     * Sauvegarde un produit extra
     */
    public ProduitExtra saveProduit(ProduitExtra produitExtra) {
        return produitExtraRepository.save(produitExtra);
    }

    /**
     * Supprime un produit extra
     */
    public void deleteProduit(Integer id) {
        produitExtraRepository.deleteById(id);
    }
}
