package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "voiture")
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "matricule")
    private String matricule;

    private Integer capacite;

    @OneToMany(mappedBy = "voiture")
    private List<Voyage> voyages;

    @OneToMany(mappedBy = "voiture")
    private List<PlaceVoiture> placeVoitures;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }

    public List<Voyage> getVoyages() { return voyages; }
    public void setVoyages(List<Voyage> voyages) { this.voyages = voyages; }

    public List<PlaceVoiture> getPlaceVoitures() { return placeVoitures; }
    public void setPlaceVoitures(List<PlaceVoiture> placeVoitures) { this.placeVoitures = placeVoitures; }
}