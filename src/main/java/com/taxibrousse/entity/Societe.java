package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "societe")
public class Societe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lib")
    private String lib;

    @Column(name = "prixUnitaire_pub", precision = 10, scale = 2)
    private BigDecimal prixUnitairePub;

    @OneToMany(mappedBy = "societe")
    private List<Publicite> publicites;

    @OneToMany(mappedBy = "societe")
    private List<Paiement> paiements;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLib() { return lib; }
    public void setLib(String lib) { this.lib = lib; }

    public BigDecimal getPrixUnitairePub() { return prixUnitairePub; }
    public void setPrixUnitairePub(BigDecimal prixUnitairePub) { this.prixUnitairePub = prixUnitairePub; }

    public List<Publicite> getPublicites() { return publicites; }
    public void setPublicites(List<Publicite> publicites) { this.publicites = publicites; }

    public List<Paiement> getPaiements() { return paiements; }
    public void setPaiements(List<Paiement> paiements) { this.paiements = paiements; }

    // Méthode utilitaire pour calculer le montant total des publicités
    public BigDecimal getMontantTotalPublicites() {
        if (publicites == null || publicites.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return publicites.stream()
                .map(Publicite::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du total des paiements effectués
    public BigDecimal getTotalPaiements() {
        if (paiements == null || paiements.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return paiements.stream()
                .map(Paiement::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du reste à payer global
    public BigDecimal getResteAPayer() {
        return getMontantTotalPublicites().subtract(getTotalPaiements());
    }

    // Calcul du pourcentage payé (prorata)
    public BigDecimal getPourcentagePaye() {
        BigDecimal total = getMontantTotalPublicites();
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getTotalPaiements()
                .multiply(BigDecimal.valueOf(100))
                .divide(total, 4, java.math.RoundingMode.HALF_UP);
    }
}
