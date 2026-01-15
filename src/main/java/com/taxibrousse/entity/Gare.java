package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "gare")
public class Gare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Column
    private Integer ville;

    // Relations
    @OneToMany(mappedBy = "depart")
    private List<Trajet> trajetsDepart;

    @OneToMany(mappedBy = "arrive")
    private List<Trajet> trajetsArrive;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getVille() { return ville; }
    public void setVille(Integer ville) { this.ville = ville; }

    public List<Trajet> getTrajetsDepart() { return trajetsDepart; }
    public void setTrajetsDepart(List<Trajet> trajetsDepart) { this.trajetsDepart = trajetsDepart; }

    public List<Trajet> getTrajetsArrive() { return trajetsArrive; }
    public void setTrajetsArrive(List<Trajet> trajetsArrive) { this.trajetsArrive = trajetsArrive; }
}