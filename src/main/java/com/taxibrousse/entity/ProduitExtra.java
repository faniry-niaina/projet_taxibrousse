package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produit_extra")
public class ProduitExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lib", nullable = false)
    private String lib;

    @Column(name = "prix", nullable = false)
    private BigDecimal prix;

    // Constructeurs
    public ProduitExtra() {}

    public ProduitExtra(String lib, BigDecimal prix) {
        this.lib = lib;
        this.prix = prix;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLib() { return lib; }
    public void setLib(String lib) { this.lib = lib; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
}
