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

    @OneToMany(mappedBy = "voyage")
    private List<Publicite> publicites;

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

    public List<Publicite> getPublicites() { return publicites; }
    public void setPublicites(List<Publicite> publicites) { this.publicites = publicites; }

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
            
            // Trouver le prix adulte pour ce type de place (pour calculer le prix senior)
            BigDecimal prixAdulte = BigDecimal.ZERO;
            if (pv.getPlaceVoitureCats() != null) {
                prixAdulte = pv.getPlaceVoitureCats().stream()
                        .filter(pvc -> pvc.getCategorie() != null && 
                                       pvc.getCategorie().getId().equals(PlaceVoitureCat.ID_ADULTE))
                        .map(PlaceVoitureCat::getPrix)
                        .findFirst()
                        .orElse(BigDecimal.ZERO);
            }
            
            // Ajouter les prix par catégorie
            List<Map<String, Object>> prixParCategorie = new ArrayList<>();
            if (pv.getPlaceVoitureCats() != null) {
                for (PlaceVoitureCat pvc : pv.getPlaceVoitureCats()) {
                    Map<String, Object> prixInfo = new HashMap<>();
                    prixInfo.put("categorie", pvc.getCategorie());
                    prixInfo.put("categorieLib", pvc.getCategorie() != null ? pvc.getCategorie().getLib() : "Inconnu");
                    
                    // Pour les seniors, calculer le prix réel basé sur le prix adulte
                    BigDecimal prixAffiche;
                    if (pvc.isSenior() && prixAdulte.compareTo(BigDecimal.ZERO) > 0) {
                        prixAffiche = pvc.calculerPrixSenior(prixAdulte);
                    } else {
                        prixAffiche = pvc.getPrix() != null ? pvc.getPrix() : BigDecimal.ZERO;
                    }
                    prixInfo.put("prix", prixAffiche);
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
    // On prend le prix le plus élevé parmi les catégories pour chaque type (en calculant le prix réel pour les seniors)
    public BigDecimal getChiffreAffairesMax() {
        if (voiture == null || voiture.getPlaceVoitures() == null) return BigDecimal.ZERO;
        
        return voiture.getPlaceVoitures().stream()
                .map(pv -> {
                    BigDecimal nbPlaces = BigDecimal.valueOf(pv.getNombrePlace() != null ? pv.getNombrePlace() : 0);
                    
                    // Trouver le prix adulte pour ce type de place
                    BigDecimal prixAdulte = BigDecimal.ZERO;
                    if (pv.getPlaceVoitureCats() != null) {
                        prixAdulte = pv.getPlaceVoitureCats().stream()
                                .filter(pvc -> pvc.getCategorie() != null && 
                                               pvc.getCategorie().getId().equals(PlaceVoitureCat.ID_ADULTE))
                                .map(PlaceVoitureCat::getPrix)
                                .findFirst()
                                .orElse(BigDecimal.ZERO);
                    }
                    
                    // Trouver le prix max parmi les catégories (en calculant le prix réel pour les seniors)
                    BigDecimal prixMax = BigDecimal.ZERO;
                    final BigDecimal prixAdulteFinal = prixAdulte;
                    if (pv.getPlaceVoitureCats() != null) {
                        prixMax = pv.getPlaceVoitureCats().stream()
                                .map(pvc -> {
                                    if (pvc.isSenior() && prixAdulteFinal.compareTo(BigDecimal.ZERO) > 0) {
                                        return pvc.calculerPrixSenior(prixAdulteFinal);
                                    }
                                    return pvc.getPrix() != null ? pvc.getPrix() : BigDecimal.ZERO;
                                })
                                .max(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);
                    }
                    return nbPlaces.multiply(prixMax);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du chiffre d'affaires actuel (places réservées * prix selon catégorie)
    // Pour les seniors, le prix est calculé : prix_adulte - (prix_adulte × pourcentage/100)
    public BigDecimal getChiffreAffairesActuel() {
        if (reservations == null || reservations.isEmpty() || voiture == null || voiture.getPlaceVoitures() == null) {
            return BigDecimal.ZERO;
        }
        
        return reservations.stream()
                .map(r -> {
                    if (r.getTypePlace() == null || r.getCategorie() == null) return BigDecimal.ZERO;
                    
                    // Trouver le PlaceVoiture correspondant au type de place
                    PlaceVoiture placeVoiture = voiture.getPlaceVoitures().stream()
                            .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId().equals(r.getTypePlace().getId()))
                            .findFirst()
                            .orElse(null);
                    
                    if (placeVoiture == null || placeVoiture.getPlaceVoitureCats() == null) return BigDecimal.ZERO;
                    
                    // Trouver le prix adulte pour calculer le prix senior si nécessaire
                    BigDecimal prixAdulte = placeVoiture.getPlaceVoitureCats().stream()
                            .filter(pvc -> pvc.getCategorie() != null && 
                                           pvc.getCategorie().getId().equals(PlaceVoitureCat.ID_ADULTE))
                            .map(PlaceVoitureCat::getPrix)
                            .findFirst()
                            .orElse(BigDecimal.ZERO);
                    
                    // Trouver le PlaceVoitureCat pour la catégorie de la réservation
                    PlaceVoitureCat pvc = placeVoiture.getPlaceVoitureCats().stream()
                            .filter(p -> p.getCategorie() != null && p.getCategorie().getId().equals(r.getCategorie().getId()))
                            .findFirst()
                            .orElse(null);
                    
                    if (pvc == null) return BigDecimal.ZERO;
                    
                    // Calculer le prix réel
                    BigDecimal prixPlace;
                    if (pvc.isSenior() && prixAdulte.compareTo(BigDecimal.ZERO) > 0) {
                        prixPlace = pvc.calculerPrixSenior(prixAdulte);
                    } else {
                        prixPlace = pvc.getPrix() != null ? pvc.getPrix() : BigDecimal.ZERO;
                    }
                    
                    return BigDecimal.valueOf(r.getNbPlaces()).multiply(prixPlace);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du montant généré par diffusion de publicités
    public BigDecimal getMontantPublicites() {
        if (publicites == null || publicites.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return publicites.stream()
                .map(Publicite::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du montant payé des publicités pour ce voyage (prorata)
    public BigDecimal getMontantPublicitesPaye() {
        if (publicites == null || publicites.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return publicites.stream()
                .map(Publicite::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du reste à payer des publicités pour ce voyage (prorata)
    public BigDecimal getMontantPublicitesRestant() {
        if (publicites == null || publicites.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return publicites.stream()
                .map(Publicite::getResteAPayer)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcul du chiffre d'affaires total (tickets vendus + publicités)
    public BigDecimal getChiffreAffairesTotal() {
        return getChiffreAffairesActuel().add(getMontantPublicites());
    }
}