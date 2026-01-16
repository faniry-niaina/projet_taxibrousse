package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "place_voiture_cat")
public class PlaceVoitureCat {

    // Constantes pour les catégories
    public static final int ID_ADULTE = 1;
    public static final int ID_ENFANT = 2;
    public static final int ID_SENIOR = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "id_place_voiture")
    private PlaceVoiture placeVoiture;

    @Column(precision = 10, scale = 2)
    private BigDecimal prix;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public PlaceVoiture getPlaceVoiture() { return placeVoiture; }
    public void setPlaceVoiture(PlaceVoiture placeVoiture) { this.placeVoiture = placeVoiture; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    /**
     * Calcule le prix réel pour un senior basé sur le prix adulte.
     * Pour les seniors, le champ 'prix' stocke le pourcentage de réduction.
     * Exemple: si prix = 20 et prixAdulte = 10000, le prix réel sera 8000 (10000 - 20%)
     * 
     * @param prixAdulte le prix de référence (prix adulte)
     * @return le prix réel après application de la réduction
     */
    public BigDecimal calculerPrixSenior(BigDecimal prixAdulte) {
        if (prixAdulte == null || this.prix == null) {
            return BigDecimal.ZERO;
        }
        // prix stocké = pourcentage de réduction (ex: 20 pour 20%)
        BigDecimal pourcentage = this.prix.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal reduction = prixAdulte.multiply(pourcentage);
        return prixAdulte.subtract(reduction).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Vérifie si cette entrée correspond à la catégorie Senior
     */
    public boolean isSenior() {
        return this.categorie != null && this.categorie.getId() != null 
               && this.categorie.getId().equals(ID_SENIOR);
    }
}
