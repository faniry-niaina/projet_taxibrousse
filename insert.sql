-- Active: 1752483638349@@127.0.0.1@5432@taxibrousse
-- =================================
-- 1️⃣ STATUS (pour réservation)
-- =================================
INSERT INTO status (id, lib) VALUES
(1, 1),  -- RESERVE
(2, 2),  -- EN_ATTENTE
(3, 3);  -- ANNULE

-- =================================
-- 2️⃣ TYPE_PLACE
-- =================================
INSERT INTO type_place (id, libelle) VALUES
(1, 'Standard'),
(2, 'Premium'),
(3,'VIP');

-- =================================
-- 3️⃣ GARES
-- =================================
INSERT INTO gare (id, nom, ville) VALUES
(1, 'Antananarivo', 1),
(2, 'Toamasina', 2),
(3, 'Fianarantsoa', 3),
(4, 'Mahajanga', 4),
(5, 'Toliara', 5);

-- =================================
-- 4️⃣ VOITURES
-- =================================
INSERT INTO voiture (id, matricule, capacite) VALUES
(1, 'TNB-001', 18);

-- =================================
-- 5️⃣ PLACE_VOITURE
-- =================================
INSERT INTO place_voiture (id, id_type_place, prix, id_voiture, nombre_place) VALUES
(1, 1, 80000.00, 1, 10),   -- Standard pour voiture 1
(2, 2, 140000.00, 1, 6),   -- VIP pour voiture 1
(3, 3, 180000.00, 1, 2);   -- VIP pour voiture 3




-- =================================
-- 6️⃣ TRAJETS
-- =================================
INSERT INTO trajet (id, id_depart, id_arrive) VALUES
(1, 1, 2),  -- Antananarivo → Toamasina
(2, 1, 3),  -- Antananarivo → Fianarantsoa
(3, 1, 4),  -- Antananarivo → Mahajanga
(4, 1, 5),  -- Antananarivo → Toliara
(5, 2, 3),  -- Toamasina → Fianarantsoa
(6, 3, 5),  -- Fianarantsoa → Toliara
(7, 4, 1),  -- Mahajanga → Antananarivo
(8, 5, 1);  -- Toliara → Antananarivo

-- =================================
-- 7️⃣ VOYAGES
-- =================================
INSERT INTO voyage (id, date_heure, prix, id_voiture, id_trajet, dure_voyage) VALUES
(1, '2026-01-15', 30000.00, 1, 1, 6);  -- Antananarivo → Toamasina


-- =================================
-- 8️⃣ RÉSERVATIONS (EXEMPLE)
-- =================================
INSERT INTO reservation (id, nomClient, nbPlaces, idVoyage, idStatus, type_place) VALUES
(1, 'Rakoto Jean', 2, 1, 1, 1),      -- Standard
(2, 'Rabe Paul', 3, 1, 1, 2),        -- VIP
(3, 'Ranaivo Lala', 1, 2, 1, 1),     -- Standard
(4, 'Andrianina Fanja', 2, 3, 1, 2); -- VIP