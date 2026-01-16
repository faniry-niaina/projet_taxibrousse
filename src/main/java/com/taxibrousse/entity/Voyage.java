package com.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private LocalDateTime dateHeure;

    @Column(name = "dureVoyage", nullable = false)
    private Integer dureVoyage; // en heures

    @OneToMany(mappedBy = "voyage")
    private List<Reservation> reservations;

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Voiture getVoiture() { return voiture; }
    public void setVoiture(Voiture voiture) { this.voiture = voiture; }

    public Trajet getTrajet() { return trajet; }
    public void setTrajet(Trajet trajet) { this.trajet = trajet; }

    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }

    public Integer getDureVoyage() { return dureVoyage; }
    public void setDureVoyage(Integer dureVoyage) { this.dureVoyage = dureVoyage; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }

    // Calcul des places restantes totales
    public int getPlacesRestantes() {
        int placesReservees = reservations != null ? reservations.stream().mapToInt(Reservation::getNbPlaces).sum() : 0;
        return voiture.getCapacite() - placesReservees;
    }

    // Retourne la liste des infos de places par type (pour boucler dans Thymeleaf)
    public List<Map<String, Object>> getPlacesParType() {
        List<Map<String, Object>> result = new ArrayList<>();
        if (voiture == null || voiture.getPlaceVoitures() == null) return result;
        
        for (PlaceVoiture pv : voiture.getPlaceVoitures()) {
            Map<String, Object> info = new HashMap<>();
            info.put("placeVoiture", pv);
            info.put("typePlace", pv.getTypePlace());
            info.put("libelle", pv.getTypePlace() != null ? pv.getTypePlace().getLibelle() : "Inconnu");
            info.put("total", pv.getNombrePlace() != null ? pv.getNombrePlace() : 0);
            
            // Calculer les places restantes pour ce type (toutes catégories confondues)
            int placesReservees = reservations != null ? reservations.stream()
                    .filter(r -> r.getTypePlace() != null && pv.getTypePlace() != null 
                            && r.getTypePlace().getId().equals(pv.getTypePlace().getId()))
                    .mapToInt(Reservation::getNbPlaces)
                    .sum() : 0;
            int restantes = (pv.getNombrePlace() != null ? pv.getNombrePlace() : 0) - placesReservees;
            info.put("restantes", restantes);
            
            // Ajouter les prix par catégorie
            List<Map<String, Object>> prixParCategorie = new ArrayList<>();
            if (pv.getPlaceVoitureCats() != null) {
                for (PlaceVoitureCat pvc : pv.getPlaceVoitureCats()) {
                    Map<String, Object> prixInfo = new HashMap<>();
                    prixInfo.put("categorie", pvc.getCategorie());
                    prixInfo.put("categorieLib", pvc.getCategorie() != null ? pvc.getCategorie().getLib() : "Inconnu");
                    prixInfo.put("prix", pvc.getPrix() != null ? pvc.getPrix() : BigDecimal.ZERO);
                    prixParCategorie.add(prixInfo);
                }
            }
            info.put("prixParCategorie", prixParCategorie);
            
            result.add(info);
        }
        return result;
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

    // Calcul du chiffre d'affaires maximum (somme des prix max par type de place * nombre de places)
    // On prend le prix le plus élevé parmi les catégories pour chaque type
    public BigDecimal getChiffreAffairesMax() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return BigDecimal.ZERO;
        
        return voiture.getPlaceVoitures().stream()
                .map(pv -> {
                    BigDecimal nbPlaces = BigDecimal.valueOf(pv.getNombrePlace() != null ? pv.getNombrePlace() : 0);
                    // Trouver le prix max parmi les catégories
                    BigDecimal prixMax = BigDecimal.ZERO;
                    if (pv.getPlaceVoitureCats() != null) {
                        prixMax = pv.getPlaceVoitureCats().stream()
                                .map(pvc -> pvc.getPrix() != null ? pvc.getPrix() : BigDecimal.ZERO)
                                .max(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);
                    }
                    return nbPlaces.multiply(prixMax);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du chiffre d'affaires actuel (places réservées * prix selon catégorie)
    public BigDecimal getChiffreAffairesActuel() {
        if (reservations == null || reservations.isEmpty() || voiture == null || voiture.getPlaceVoitures() == null) {
            return BigDecimal.ZERO;
        }
        
        return reservations.stream()
                .map(r -> {
                    if (r.getTypePlace() == null || r.getCategorie() == null) return BigDecimal.ZERO;
                    
                    // Trouver le prix correspondant au type de place ET à la catégorie
                    BigDecimal prixPlace = voiture.getPlaceVoitures().stream()
                            .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId().equals(r.getTypePlace().getId()))
                            .flatMap(pv -> pv.getPlaceVoitureCats() != null ? pv.getPlaceVoitureCats().stream() : java.util.stream.Stream.empty())
                            .filter(pvc -> pvc.getCategorie() != null && pvc.getCategorie().getId().equals(r.getCategorie().getId()))
                            .map(PlaceVoitureCat::getPrix)
                            .findFirst()
                            .orElse(BigDecimal.ZERO);
                    
                    return BigDecimal.valueOf(r.getNbPlaces()).multiply(prixPlace);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}