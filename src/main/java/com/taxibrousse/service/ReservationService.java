package com.taxibrousse.service;

import com.taxibrousse.entity.Reservation;
import com.taxibrousse.entity.Status;
import com.taxibrousse.entity.TypePlace;
import com.taxibrousse.entity.Voyage;
import com.taxibrousse.repository.ReservationRepository;
import com.taxibrousse.repository.StatusRepository;
import com.taxibrousse.repository.TypePlaceRepository;
import com.taxibrousse.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Créer une réservation avec status par défaut (id=1)
    // Retourne un message d'erreur ou null si succès
    public String reserver(Integer voyageId, String nomClient, int nbPlaces, Integer typePlaceId) {
        Voyage voyage = voyageRepository.findById(voyageId).orElse(null);
        if (voyage == null) {
            return "Voyage introuvable";
        }

        if (typePlaceId == null) {
            return "Veuillez sélectionner un type de place (Standard ou VIP)";
        }

        TypePlace typePlace = typePlaceRepository.findById(typePlaceId).orElse(null);
        if (typePlace == null) {
            return "Type de place invalide";
        }

        // Vérifier les places disponibles selon le type
        int placesDisponibles;
        String nomType = typePlace.getLibelle();
        
        if (typePlaceId == 1) { // Standard
            placesDisponibles = voyage.getPlacesStandardRestantes();
        } else if (typePlaceId == 2) { // VIP
            placesDisponibles = voyage.getPlacesVipRestantes();
        } else {
            placesDisponibles = voyage.getPlacesRestantes();
        }

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

        reservationRepository.save(reservation);
        return null; // Succès
    }

    public List<Reservation> getReservationsByVoyage(Integer voyageId) {
        return reservationRepository.findByVoyageId(voyageId);
    }

    public List<TypePlace> getAllTypePlaces() {
        return typePlaceRepository.findAll();
    }
}