package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vente_produit")
public class VenteProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_produit", nullable = false)
    private ProduitExtra produit;

    @ManyToOne
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;

    @Column(name = "date_vente", nullable = false)
    private LocalDate dateVente;

    @Column(name = "nombre", nullable = false)
    private Integer nombre;

    // Constructeurs
    public VenteProduit() {}

    public VenteProduit(ProduitExtra produit, Voyage voyage, LocalDate dateVente, Integer nombre) {
        this.produit = produit;
        this.voyage = voyage;
        this.dateVente = dateVente;
        this.nombre = nombre;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ProduitExtra getProduit() { return produit; }
    public void setProduit(ProduitExtra produit) { this.produit = produit; }

    public Voyage getVoyage() { return voyage; }
    public void setVoyage(Voyage voyage) { this.voyage = voyage; }

    public LocalDate getDateVente() { return dateVente; }
    public void setDateVente(LocalDate dateVente) { this.dateVente = dateVente; }

    public Integer getNombre() { return nombre; }
    public void setNombre(Integer nombre) { this.nombre = nombre; }
}
