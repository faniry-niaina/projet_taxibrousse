package com.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nomClient", nullable = false)
    private String nomClient;

    @Column(name = "nbPlaces", nullable = false)
    private Integer nbPlaces;

    @ManyToOne
    @JoinColumn(name = "idVoyage", nullable = false)
    private Voyage voyage;

    @ManyToOne
    @JoinColumn(name = "idStatus", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "type_place")
    private TypePlace typePlace;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public Integer getNbPlaces() { return nbPlaces; }
    public void setNbPlaces(Integer nbPlaces) { this.nbPlaces = nbPlaces; }

    public Voyage getVoyage() { return voyage; }
    public void setVoyage(Voyage voyage) { this.voyage = voyage; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public TypePlace getTypePlace() { return typePlace; }
    public void setTypePlace(TypePlace typePlace) { this.typePlace = typePlace; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
}