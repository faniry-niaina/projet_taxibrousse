package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorie")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lib")
    private String lib;

    @OneToMany(mappedBy = "categorie")
    private List<PlaceVoitureCat> placeVoitureCats;

    @OneToMany(mappedBy = "categorie")
    private List<Reservation> reservations;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLib() { return lib; }
    public void setLib(String lib) { this.lib = lib; }

    public List<PlaceVoitureCat> getPlaceVoitureCats() { return placeVoitureCats; }
    public void setPlaceVoitureCats(List<PlaceVoitureCat> placeVoitureCats) { this.placeVoitureCats = placeVoitureCats; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}
