package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "publicites")
public class Publicite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @Column(name = "nombre", nullable = false)
    private Integer nombre;

    @Column(name = "date_pub", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Societe getSociete() { return societe; }
    public void setSociete(Societe societe) { this.societe = societe; }

    public Integer getNombre() { return nombre; }
    public void setNombre(Integer nombre) { this.nombre = nombre; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Voyage getVoyage() { return voyage; }
    public void setVoyage(Voyage voyage) { this.voyage = voyage; }

    // Calcul du montant total de la publicité
    public BigDecimal getMontantTotal() {
        if (societe == null || societe.getPrixUnitairePub() == null || nombre == null) {
            return BigDecimal.ZERO;
        }
        return societe.getPrixUnitairePub().multiply(BigDecimal.valueOf(nombre));
    }

    // Calcul du montant payé pour cette publicité (prorata)
    public BigDecimal getMontantPaye() {
        if (societe == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal pourcentagePaye = societe.getPourcentagePaye();
        return getMontantTotal()
                .multiply(pourcentagePaye)
                .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
    }

    // Calcul du reste à payer pour cette publicité (prorata)
    public BigDecimal getResteAPayer() {
        return getMontantTotal().subtract(getMontantPaye());
    }
}
