package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "place_voiture")
public class PlaceVoiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_type_place")
    private TypePlace typePlace;

    @Column(precision = 10, scale = 2)
    private BigDecimal prix;

    @ManyToOne
    @JoinColumn(name = "id_voiture")
    private Voiture voiture;

    @Column(name = "nombre_place")
    private Integer nombrePlace;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public TypePlace getTypePlace() { return typePlace; }
    public void setTypePlace(TypePlace typePlace) { this.typePlace = typePlace; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public Voiture getVoiture() { return voiture; }
    public void setVoiture(Voiture voiture) { this.voiture = voiture; }

    public Integer getNombrePlace() { return nombrePlace; }
    public void setNombrePlace(Integer nombrePlace) { this.nombrePlace = nombrePlace; }
}
