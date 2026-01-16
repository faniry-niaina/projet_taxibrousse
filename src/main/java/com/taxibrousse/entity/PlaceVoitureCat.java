package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "place_voiture_cat")
public class PlaceVoitureCat {

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
}
