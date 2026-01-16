package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "place_voiture")
public class PlaceVoiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_type_place")
    private TypePlace typePlace;

    @ManyToOne
    @JoinColumn(name = "id_voiture")
    private Voiture voiture;

    @Column(name = "nombre_place")
    private Integer nombrePlace;

    @OneToMany(mappedBy = "placeVoiture")
    private List<PlaceVoitureCat> placeVoitureCats;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public TypePlace getTypePlace() { return typePlace; }
    public void setTypePlace(TypePlace typePlace) { this.typePlace = typePlace; }

    public Voiture getVoiture() { return voiture; }
    public void setVoiture(Voiture voiture) { this.voiture = voiture; }

    public Integer getNombrePlace() { return nombrePlace; }
    public void setNombrePlace(Integer nombrePlace) { this.nombrePlace = nombrePlace; }

    public List<PlaceVoitureCat> getPlaceVoitureCats() { return placeVoitureCats; }
    public void setPlaceVoitureCats(List<PlaceVoitureCat> placeVoitureCats) { this.placeVoitureCats = placeVoitureCats; }
}
