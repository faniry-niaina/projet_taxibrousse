package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "type_place")
public class TypePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "libelle")
    private String libelle;

    @OneToMany(mappedBy = "typePlace")
    private List<PlaceVoiture> placeVoitures;

    @OneToMany(mappedBy = "typePlace")
    private List<Reservation> reservations;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public List<PlaceVoiture> getPlaceVoitures() { return placeVoitures; }
    public void setPlaceVoitures(List<PlaceVoiture> placeVoitures) { this.placeVoitures = placeVoitures; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}