package com.taxibrousse.service;

import com.taxibrousse.entity.*;
import com.taxibrousse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TypePlaceRepository typePlaceRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private PlaceVoitureCatRepository placeVoitureCatRepository;

    // Créer une réservation avec catégorie
    // Retourne un message d'erreur ou null si succès
    @Transactional
    public String reserver(Integer voyageId, String nomClient, Integer typePlaceId, Integer categorieId, int nbPlaces) {
        Voyage voyage = voyageRepository.findById(voyageId).orElse(null);
        if (voyage == null) {
            return "Voyage introuvable";
        }

        if (typePlaceId == null) {
            return "Veuillez sélectionner un type de place";
        }

        if (categorieId == null) {
            return "Veuillez sélectionner une catégorie (Adulte/Enfant)";
        }

        TypePlace typePlace = typePlaceRepository.findById(typePlaceId).orElse(null);
        if (typePlace == null) {
            return "Type de place invalide";
        }

        Categorie categorie = categorieRepository.findById(categorieId).orElse(null);
        if (categorie == null) {
            return "Catégorie invalide";
        }

        // Vérifier les places disponibles pour ce type (toutes catégories confondues)
        int placesDisponibles = getPlacesRestantesByType(voyage, typePlaceId);
        String nomType = typePlace.getLibelle();

        if (nbPlaces > placesDisponibles) {
            return "Désolé, il n'y a que " + placesDisponibles + " place(s) " + nomType + " disponible(s). Vous avez demandé " + nbPlaces + " place(s).";
        }

        // Status avec lib = 1 (RESERVE)
        Status statusReserve = statusRepository.findByLib(1)
                .orElseThrow(() -> new RuntimeException("Status RESERVE introuvable"));

        Reservation reservation = new Reservation();
        reservation.setVoyage(voyage);
        reservation.setNomClient(nomClient);
        reservation.setNbPlaces(nbPlaces);
        reservation.setStatus(statusReserve);
        reservation.setTypePlace(typePlace);
        reservation.setCategorie(categorie);

        reservationRepository.save(reservation);
        return null; // Succès
    }

    // Réserver plusieurs lignes à la fois
    @Transactional
    public String reserverMultiple(Integer voyageId, String nomClient, List<LigneReservation> lignes) {
        if (lignes == null || lignes.isEmpty()) {
            return "Veuillez ajouter au moins une ligne de réservation";
        }

        // Vérifier d'abord toutes les lignes
        Voyage voyage = voyageRepository.findById(voyageId).orElse(null);
        if (voyage == null) {
            return "Voyage introuvable";
        }

        for (LigneReservation ligne : lignes) {
            int placesDisponibles = getPlacesRestantesByType(voyage, ligne.getTypePlaceId());
            if (ligne.getNbPlaces() > placesDisponibles) {
                TypePlace tp = typePlaceRepository.findById(ligne.getTypePlaceId()).orElse(null);
                String nomType = tp != null ? tp.getLibelle() : "Inconnu";
                return "Désolé, il n'y a que " + placesDisponibles + " place(s) " + nomType + " disponible(s).";
            }
        }

        // Créer les réservations
        for (LigneReservation ligne : lignes) {
            String erreur = reserver(voyageId, nomClient, ligne.getTypePlaceId(), ligne.getCategorieId(), ligne.getNbPlaces());
            if (erreur != null) {
                return erreur;
            }
        }

        return null; // Succès
    }

    private int getPlacesRestantesByType(Voyage voyage, Integer typePlaceId) {
        if (voyage.getVoiture() == null || voyage.getVoiture().getPlaceVoitures() == null) return 0;
        
        int placesTotal = voyage.getVoiture().getPlaceVoitures().stream()
                .filter(pv -> pv.getTypePlace() != null && pv.getTypePlace().getId().equals(typePlaceId))
                .mapToInt(PlaceVoiture::getNombrePlace)
                .sum();
        
        int placesReservees = voyage.getReservations() != null ? voyage.getReservations().stream()
                .filter(r -> r.getTypePlace() != null && r.getTypePlace().getId().equals(typePlaceId))
                .mapToInt(Reservation::getNbPlaces)
                .sum() : 0;
        
        return placesTotal - placesReservees;
    }

    public List<Reservation> getReservationsByVoyage(Integer voyageId) {
        return reservationRepository.findByVoyageId(voyageId);
    }

    public List<TypePlace> getAllTypePlaces() {
        return typePlaceRepository.findAll();
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    /**
     * Calcule le prix unitaire pour un voyage, un type de place et une catégorie donnés.
     * Pour les seniors (catégorie id=3), le prix est calculé comme :
     * prix_senior = prix_adulte - (prix_adulte × pourcentage_reduction / 100)
     * 
     * @param voitureId l'ID de la voiture
     * @param typePlaceId l'ID du type de place
     * @param categorieId l'ID de la catégorie (1=Adulte, 2=Enfant, 3=Senior)
     * @return le prix unitaire calculé
     */
    public BigDecimal getPrixUnitaire(Integer voitureId, Integer typePlaceId, Integer categorieId) {
        List<PlaceVoitureCat> prixConfigs = placeVoitureCatRepository
                .findByPlaceVoiture_Voiture_IdAndPlaceVoiture_TypePlace_Id(voitureId, typePlaceId);
        
        if (prixConfigs.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Si c'est un senior, on doit calculer le prix basé sur le prix adulte
        if (categorieId != null && categorieId.equals(PlaceVoitureCat.ID_SENIOR)) {
            // Trouver le prix adulte
            Optional<PlaceVoitureCat> prixAdulteOpt = prixConfigs.stream()
                    .filter(p -> p.getCategorie() != null && 
                                 p.getCategorie().getId().equals(PlaceVoitureCat.ID_ADULTE))
                    .findFirst();
            
            // Trouver la config senior (qui contient le pourcentage de réduction)
            Optional<PlaceVoitureCat> configSeniorOpt = prixConfigs.stream()
                    .filter(p -> p.getCategorie() != null && 
                                 p.getCategorie().getId().equals(PlaceVoitureCat.ID_SENIOR))
                    .findFirst();
            
            if (prixAdulteOpt.isPresent() && configSeniorOpt.isPresent()) {
                BigDecimal prixAdulte = prixAdulteOpt.get().getPrix();
                return configSeniorOpt.get().calculerPrixSenior(prixAdulte);
            }
        }

        // Pour les autres catégories (Adulte, Enfant), on retourne directement le prix stocké
        return prixConfigs.stream()
                .filter(p -> p.getCategorie() != null && p.getCategorie().getId().equals(categorieId))
                .findFirst()
                .map(PlaceVoitureCat::getPrix)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Calcule le prix total d'une réservation
     * @param voitureId l'ID de la voiture
     * @param typePlaceId l'ID du type de place
     * @param categorieId l'ID de la catégorie
     * @param nbPlaces le nombre de places
     * @return le prix total
     */
    public BigDecimal calculerPrixTotal(Integer voitureId, Integer typePlaceId, Integer categorieId, int nbPlaces) {
        BigDecimal prixUnitaire = getPrixUnitaire(voitureId, typePlaceId, categorieId);
        return prixUnitaire.multiply(new BigDecimal(nbPlaces));
    }

    // Classe interne pour représenter une ligne de réservation
    public static class LigneReservation {
        private Integer typePlaceId;
        private Integer categorieId;
        private int nbPlaces;

        public LigneReservation() {}

        public LigneReservation(Integer typePlaceId, Integer categorieId, int nbPlaces) {
            this.typePlaceId = typePlaceId;
            this.categorieId = categorieId;
            this.nbPlaces = nbPlaces;
        }

        public Integer getTypePlaceId() { return typePlaceId; }
        public void setTypePlaceId(Integer typePlaceId) { this.typePlaceId = typePlaceId; }

        public Integer getCategorieId() { return categorieId; }
        public void setCategorieId(Integer categorieId) { this.categorieId = categorieId; }

        public int getNbPlaces() { return nbPlaces; }
        public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }
    }
}