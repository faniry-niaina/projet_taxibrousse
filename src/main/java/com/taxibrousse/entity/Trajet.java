package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "trajet")
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idDepart", nullable = false)
    private Gare depart;

    @ManyToOne
    @JoinColumn(name = "idArrive", nullable = false)
    private Gare arrive;

    @OneToMany(mappedBy = "trajet")
    private List<Voyage> voyages;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Gare getDepart() { return depart; }
    public void setDepart(Gare depart) { this.depart = depart; }

    public Gare getArrive() { return arrive; }
    public void setArrive(Gare arrive) { this.arrive = arrive; }

    public List<Voyage> getVoyages() { return voyages; }
    public void setVoyages(List<Voyage> voyages) { this.voyages = voyages; }
}