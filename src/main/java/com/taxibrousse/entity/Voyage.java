package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "voyage")
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idVoiture", nullable = false)
    private Voiture voiture;

    @ManyToOne
    @JoinColumn(name = "idTrajet", nullable = false)
    private Trajet trajet;

    @Column(name = "dateHeure", nullable = false)
    private LocalDate dateHeure;

    @Column(name = "dureVoyage", nullable = false)
    private Integer dureVoyage; // en heures

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @OneToMany(mappedBy = "voyage")
    private List<Reservation> reservations;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Voiture getVoiture() { return voiture; }
    public void setVoiture(Voiture voiture) { this.voiture = voiture; }

    public Trajet getTrajet() { return trajet; }
    public void setTrajet(Trajet trajet) { this.trajet = trajet; }

    public LocalDate getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDate dateHeure) { this.dateHeure = dateHeure; }

    public Integer getDureVoyage() { return dureVoyage; }
    public void setDureVoyage(Integer dureVoyage) { this.dureVoyage = dureVoyage; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }

    // Calcul des places restantes totales
    public int getPlacesRestantes() {
        int placesReservees = reservations != null ? reservations.stream().mapToInt(Reservation::getNbPlaces).sum() : 0;
        return voiture.getCapacite() - placesReservees;
    }

    // Capacité totale Standard de la voiture
    public int getPlacesStandardTotal() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return 0;
        return voiture.getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId() == 1)
                .mapToInt(PlaceVoiture::getNombrePlace)
                .sum();
    }

    // Capacité totale VIP de la voiture
    public int getPlacesVipTotal() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return 0;
        return voiture.getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId() == 2)
                .mapToInt(PlaceVoiture::getNombrePlace)
                .sum();
    }

    // Prix unitaire Standard
    public BigDecimal getPrixStandard() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return BigDecimal.ZERO;
        return voiture.getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId() == 1)
                .map(PlaceVoiture::getPrix)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    // Prix unitaire VIP
    public BigDecimal getPrixVip() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return BigDecimal.ZERO;
        return voiture.getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId() == 2)
                .map(PlaceVoiture::getPrix)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    // Calcul des places Standard restantes (type_place = 1)
    public int getPlacesStandardRestantes() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return 0;
        
        int placesStandardTotal = voiture.getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId() == 1)
                .mapToInt(PlaceVoiture::getNombrePlace)
                .sum();
        
        int placesStandardReservees = reservations != null ? reservations.stream()
                .filter(r -> r.getTypePlace() != null && r.getTypePlace().getId() == 1)
                .mapToInt(Reservation::getNbPlaces)
                .sum() : 0;
        
        return placesStandardTotal - placesStandardReservees;
    }

    // Calcul des places VIP restantes (type_place = 2)
    public int getPlacesVipRestantes() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return 0;
        
        int placesVipTotal = voiture.getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId() == 2)
                .mapToInt(PlaceVoiture::getNombrePlace)
                .sum();
        
        int placesVipReservees = reservations != null ? reservations.stream()
                .filter(r -> r.getTypePlace() != null && r.getTypePlace().getId() == 2)
                .mapToInt(Reservation::getNbPlaces)
                .sum() : 0;
        
        return placesVipTotal - placesVipReservees;
    }

    // Calcul du chiffre d'affaires maximum (nombre de places * prix pour chaque type)
    public BigDecimal getChiffreAffairesMax() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return BigDecimal.ZERO;
        
        return voiture.getPlaceVoitures().stream()
                .map(pv -> {
                    BigDecimal nbPlaces = BigDecimal.valueOf(pv.getNombrePlace() != null ? pv.getNombrePlace() : 0);
                    BigDecimal prixPlace = pv.getPrix() != null ? pv.getPrix() : BigDecimal.ZERO;
                    return nbPlaces.multiply(prixPlace);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du chiffre d'affaires actuel (places réservées * prix par type)
    public BigDecimal getChiffreAffairesActuel() {
        if (reservations == null || reservations.isEmpty() || voiture == null || voiture.getPlaceVoitures() == null) {
            return BigDecimal.ZERO;
        }
        
        return reservations.stream()
                .map(r -> {
                    if (r.getTypePlace() == null) return BigDecimal.ZERO;
                    
                    // Trouver le prix correspondant au type de place dans la voiture
                    BigDecimal prixPlace = voiture.getPlaceVoitures().stream()
                            .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId().equals(r.getTypePlace().getId()))
                            .map(PlaceVoiture::getPrix)
                            .findFirst()
                            .orElse(BigDecimal.ZERO);
                    
                    return BigDecimal.valueOf(r.getNbPlaces()).multiply(prixPlace);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}