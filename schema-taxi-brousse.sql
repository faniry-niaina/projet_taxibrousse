-- ============================================================================
-- SCHEMA TAXI BROUSSE - PostgreSQL (Français)
-- Gestion de réservation de taxi brousse à Madagascar
-- ============================================================================

-- Suppression des tables existantes (optionnel - pour développement)
-- DROP SCHEMA IF EXISTS public CASCADE;
-- CREATE SCHEMA public;

-- ============================================================================
-- 1. TABLE DES UTILISATEURS
-- ============================================================================
CREATE TABLE utilisateurs (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe_hash VARCHAR(255) NOT NULL,
    telephone VARCHAR(20) UNIQUE,
    nom_complet VARCHAR(255) NOT NULL,
    type_utilisateur VARCHAR(50) NOT NULL,
    est_actif BOOLEAN DEFAULT true,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_utilisateurs_email ON utilisateurs(email);
CREATE INDEX idx_utilisateurs_type ON utilisateurs(type_utilisateur);

-- ============================================================================
-- 2. TABLE DES CHAUFFEURS
-- ============================================================================
CREATE TABLE chauffeurs (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL UNIQUE,
    numero_permis VARCHAR(50) UNIQUE NOT NULL,
    date_expiration_permis DATE,
    date_visite_medicale DATE,
    ans_experience INT,
    note_moyenne DECIMAL(3,2) DEFAULT 0,
    nombre_trajets INT DEFAULT 0,
    est_disponible BOOLEAN DEFAULT true,
    derniere_activite TIMESTAMP,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE INDEX idx_chauffeurs_disponible ON chauffeurs(est_disponible);

-- ============================================================================
-- 3. TABLE DES PASSAGERS
-- ============================================================================
CREATE TABLE passagers (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL UNIQUE,
    nombre_trajets INT DEFAULT 0,
    montant_total_depense DECIMAL(10,2) DEFAULT 0,
    type_vehicule_prefere VARCHAR(50),
    nom_contact_urgence VARCHAR(255),
    telephone_contact_urgence VARCHAR(20),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

-- ============================================================================
-- 4. TABLE DES EXPLOITANTS/OPERATEURS
-- ============================================================================
CREATE TABLE exploitants (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL UNIQUE,
    nom_entreprise VARCHAR(255) NOT NULL,
    numero_immatriculation VARCHAR(100) UNIQUE,
    numero_licence VARCHAR(100),
    adresse TEXT,
    ville VARCHAR(100),
    telephone_entreprise VARCHAR(20),
    numero_compte_bancaire VARCHAR(50),
    est_verifie BOOLEAN DEFAULT false,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

-- ============================================================================
-- 5. TABLE DES VEHICULES
-- ============================================================================
CREATE TABLE vehicules (
    id BIGSERIAL PRIMARY KEY,
    exploitant_id BIGINT NOT NULL,
    numero_immatriculation VARCHAR(50) UNIQUE NOT NULL,
    type_vehicule VARCHAR(50) NOT NULL,
    marque VARCHAR(100),
    modele VARCHAR(100),
    annee_fabrication INT,
    capacite_sieges INT NOT NULL,
    type_carburant VARCHAR(50),
    couleur VARCHAR(50),
    kilometrage INT DEFAULT 0,
    consommation_carburant DECIMAL(5,2),
    est_actif BOOLEAN DEFAULT true,
    date_derniere_maintenance DATE,
    date_prochaine_maintenance DATE,
    date_validite_inspection DATE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (exploitant_id) REFERENCES exploitants(id) ON DELETE CASCADE
);

CREATE INDEX idx_vehicules_exploitant ON vehicules(exploitant_id);
CREATE INDEX idx_vehicules_actif ON vehicules(est_actif);

-- ============================================================================
-- 6. TABLE DES ROUTES/ITINERAIRES
-- ============================================================================
CREATE TABLE routes (
    id BIGSERIAL PRIMARY KEY,
    exploitant_id BIGINT NOT NULL,
    nom_route VARCHAR(255) NOT NULL,
    ville_depart VARCHAR(100) NOT NULL,
    ville_arrivee VARCHAR(100) NOT NULL,
    distance_km DECIMAL(8,2),
    duree_estimee_minutes INT,
    ordre_route INT,
    est_active BOOLEAN DEFAULT true,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (exploitant_id) REFERENCES exploitants(id)
);

CREATE INDEX idx_routes_exploitant ON routes(exploitant_id);
CREATE INDEX idx_routes_villes ON routes(ville_depart, ville_arrivee);

-- ============================================================================
-- 7. TABLE DES STATIONS/ARRETES
-- ============================================================================
CREATE TABLE stations (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    nom_station VARCHAR(255) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    ordre_station INT NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    heure_arrivee TIME,
    heure_depart TIME,
    est_station_principale BOOLEAN DEFAULT false,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE
);

CREATE INDEX idx_stations_route ON stations(route_id);

-- ============================================================================
-- 8. TABLE DES TRAJETS/VOYAGES
-- ============================================================================
CREATE TABLE trajets (
    id BIGSERIAL PRIMARY KEY,
    vehicule_id BIGINT NOT NULL,
    chauffeur_id BIGINT NOT NULL,
    route_id BIGINT NOT NULL,
    date_trajet DATE NOT NULL,
    heure_depart TIMESTAMP NOT NULL,
    heure_arrivee TIMESTAMP,
    statut VARCHAR(50) DEFAULT 'PROGRAMMÉ',
    nombre_sieges_disponibles INT NOT NULL,
    tarif_base DECIMAL(10,2) NOT NULL,
    heure_depart_reelle TIMESTAMP,
    heure_arrivee_reelle TIMESTAMP,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicule_id) REFERENCES vehicules(id),
    FOREIGN KEY (chauffeur_id) REFERENCES chauffeurs(id),
    FOREIGN KEY (route_id) REFERENCES routes(id)
);

CREATE INDEX idx_trajets_date ON trajets(date_trajet);
CREATE INDEX idx_trajets_statut ON trajets(statut);
CREATE INDEX idx_trajets_vehicule ON trajets(vehicule_id);
CREATE INDEX idx_trajets_chauffeur ON trajets(chauffeur_id);

-- ============================================================================
-- 9. TABLE DES RESERVATIONS
-- ============================================================================
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    trajet_id BIGINT NOT NULL,
    passager_id BIGINT NOT NULL,
    station_depart_id BIGINT NOT NULL,
    station_arrivee_id BIGINT NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nombre_sieges INT NOT NULL DEFAULT 1,
    tarif_total DECIMAL(10,2) NOT NULL,
    statut_paiement VARCHAR(50) DEFAULT 'EN_ATTENTE',
    statut_reservation VARCHAR(50) DEFAULT 'ACTIF',
    motif_annulation VARCHAR(255),
    date_annulation TIMESTAMP,
    montant_remboursement DECIMAL(10,2),
    donnees_code_qr VARCHAR(500),
    numeros_sieges VARCHAR(255),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trajet_id) REFERENCES trajets(id),
    FOREIGN KEY (passager_id) REFERENCES passagers(id),
    FOREIGN KEY (station_depart_id) REFERENCES stations(id),
    FOREIGN KEY (station_arrivee_id) REFERENCES stations(id)
);

CREATE INDEX idx_reservations_trajet ON reservations(trajet_id);
CREATE INDEX idx_reservations_passager ON reservations(passager_id);
CREATE INDEX idx_reservations_statut ON reservations(statut_paiement);

-- ============================================================================
-- 10. TABLE DES PAIEMENTS
-- ============================================================================
CREATE TABLE paiements (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    methode_paiement VARCHAR(50) NOT NULL,
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reference_transaction VARCHAR(100) UNIQUE,
    reponse_passerelle TEXT,
    est_reussi BOOLEAN DEFAULT false,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

CREATE INDEX idx_paiements_reservation ON paiements(reservation_id);
CREATE INDEX idx_paiements_date ON paiements(date_paiement);

-- ============================================================================
-- 11. TABLE DE TARIFICATION
-- ============================================================================
CREATE TABLE tarifs (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    type_vehicule VARCHAR(50),
    station_depart_id BIGINT,
    station_arrivee_id BIGINT,
    prix_base DECIMAL(10,2) NOT NULL,
    supplement_par_km DECIMAL(5,2),
    est_actif BOOLEAN DEFAULT true,
    date_effet DATE,
    date_expiration DATE,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES routes(id),
    FOREIGN KEY (station_depart_id) REFERENCES stations(id),
    FOREIGN KEY (station_arrivee_id) REFERENCES stations(id)
);

-- ============================================================================
-- 12. TABLE DE MAINTENANCE DES VEHICULES
-- ============================================================================
CREATE TABLE maintenance_vehicules (
    id BIGSERIAL PRIMARY KEY,
    vehicule_id BIGINT NOT NULL,
    type_maintenance VARCHAR(50) NOT NULL,
    description TEXT,
    date_maintenance DATE NOT NULL,
    date_completion DATE,
    cout DECIMAL(10,2),
    notes TEXT,
    statut VARCHAR(50) DEFAULT 'PROGRAMMÉE',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicule_id) REFERENCES vehicules(id) ON DELETE CASCADE
);

-- ============================================================================
-- 13. TABLE DES AVIS/EVALUATIONS
-- ============================================================================
CREATE TABLE avis (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    utilisateur_evaluateur_id BIGINT NOT NULL,
    type_evalue VARCHAR(50) NOT NULL,
    id_evalue BIGINT,
    note INT NOT NULL CHECK (note >= 1 AND note <= 5),
    commentaire TEXT,
    date_avis TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    FOREIGN KEY (utilisateur_evaluateur_id) REFERENCES utilisateurs(id)
);

-- ============================================================================
-- 14. TABLE DES DOCUMENTS
-- ============================================================================
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT,
    vehicule_id BIGINT,
    type_document VARCHAR(50) NOT NULL,
    chemin_document VARCHAR(500),
    date_expiration DATE,
    est_verifie BOOLEAN DEFAULT false,
    verifie_par BIGINT,
    date_verification TIMESTAMP,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE,
    FOREIGN KEY (vehicule_id) REFERENCES vehicules(id) ON DELETE CASCADE,
    FOREIGN KEY (verifie_par) REFERENCES utilisateurs(id)
);

-- ============================================================================
-- 15. TABLE DES NOTIFICATIONS
-- ============================================================================
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL,
    type_notification VARCHAR(50) NOT NULL,
    titre VARCHAR(255),
    message TEXT,
    est_lu BOOLEAN DEFAULT false,
    date_lecture TIMESTAMP,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id) ON DELETE CASCADE
);

CREATE INDEX idx_notifications_utilisateur ON notifications(utilisateur_id, est_lu);

-- ============================================================================
-- 16. TABLE DES JOURNAUX D'AUDIT
-- ============================================================================
CREATE TABLE journaux_audit (
    id BIGSERIAL PRIMARY KEY,
    utilisateur_id BIGINT,
    action VARCHAR(255) NOT NULL,
    type_entite VARCHAR(100),
    id_entite BIGINT,
    anciennes_valeurs TEXT,
    nouvelles_valeurs TEXT,
    adresse_ip VARCHAR(45),
    agent_utilisateur TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id)
);

CREATE INDEX idx_journaux_audit_date ON journaux_audit(date_creation);
CREATE INDEX idx_journaux_audit_utilisateur ON journaux_audit(utilisateur_id);

-- ============================================================================
-- 17. TABLE DES STATISTIQUES OPERATIONNELLES
-- ============================================================================
CREATE TABLE statistiques_trajets (
    id BIGSERIAL PRIMARY KEY,
    trajet_id BIGINT NOT NULL UNIQUE,
    nombre_passagers INT DEFAULT 0,
    revenu_total DECIMAL(10,2) DEFAULT 0,
    carburant_consomme DECIMAL(8,2),
    cout_carburant DECIMAL(10,2),
    cout_operationnel DECIMAL(10,2),
    benefice_net DECIMAL(10,2),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trajet_id) REFERENCES trajets(id)
);

-- ============================================================================
-- COMMENTAIRES/DESCRIPTIONS
-- ============================================================================

COMMENT ON TABLE utilisateurs IS 'Table principale des utilisateurs du système';
COMMENT ON TABLE chauffeurs IS 'Profil détaillé des chauffeurs';
COMMENT ON TABLE passagers IS 'Profil détaillé des passagers';
COMMENT ON TABLE exploitants IS 'Profil des exploitants/opérateurs de taxi-brousse';
COMMENT ON TABLE vehicules IS 'Inventaire des véhicules';
COMMENT ON TABLE routes IS 'Définition des routes/itinéraires disponibles';
COMMENT ON TABLE stations IS 'Arrêts/stations le long des routes';
COMMENT ON TABLE trajets IS 'Trajets/voyages programmés';
COMMENT ON TABLE reservations IS 'Réservations de passagers';
COMMENT ON TABLE paiements IS 'Enregistrement des paiements';
COMMENT ON TABLE tarifs IS 'Tarification par route et type de véhicule';
COMMENT ON TABLE maintenance_vehicules IS 'Historique de maintenance des véhicules';
COMMENT ON TABLE avis IS 'Évaluations et commentaires des utilisateurs';
COMMENT ON TABLE documents IS 'Documents de vérification (permis, immatriculation, etc)';
COMMENT ON TABLE notifications IS 'Système de notifications pour les utilisateurs';
COMMENT ON TABLE journaux_audit IS 'Historique d''audit des modifications';
COMMENT ON TABLE statistiques_trajets IS 'Statistiques opérationnelles par trajet';
