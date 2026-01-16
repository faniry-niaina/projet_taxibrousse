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
(3, 'VIP');

-- =================================
-- 3️⃣ CATEGORIE (Adulte/Enfant)
-- =================================
INSERT INTO categorie (id, lib) VALUES
(1, 'Adulte'),
(2, 'Enfant');

-- =================================
-- 4️⃣ GARES
-- =================================
INSERT INTO gare (id, nom, ville) VALUES
(1, 'Antananarivo', 1),
(2, 'Toamasina', 2),
(3, 'Fianarantsoa', 3),
(4, 'Mahajanga', 4),
(5, 'Toliara', 5);

-- =================================
-- 5️⃣ VOITURES
-- =================================
INSERT INTO voiture (id, matricule, capacite) VALUES
(1, 'TNB-001', 18);

-- =================================
-- 6️⃣ PLACE_VOITURE (sans prix maintenant)
-- =================================
INSERT INTO place_voiture (id, id_type_place, id_voiture, nombre_place) VALUES
(1, 1, 1, 10),   -- Standard pour voiture 1 (10 places)
(2, 2, 1, 6),    -- Premium pour voiture 1 (6 places)
(3, 3, 1, 2);    -- VIP pour voiture 1 (2 places)

-- =================================
-- 7️⃣ PLACE_VOITURE_CAT (prix par type de place ET catégorie)
-- =================================
-- Standard
INSERT INTO place_voiture_cat (id, id_categorie, id_place_voiture, prix) VALUES
(1, 1, 1, 80000.00),    -- Standard Adulte
(2, 2, 1, 50000.00);    -- Standard Enfant

-- Premium
INSERT INTO place_voiture_cat (id, id_categorie, id_place_voiture, prix) VALUES
(3, 1, 2, 140000.00),   -- Premium Adulte
(4, 2, 2, 90000.00);    -- Premium Enfant

-- VIP
INSERT INTO place_voiture_cat (id, id_categorie, id_place_voiture, prix) VALUES
(5, 1, 3, 190000.00),   -- VIP Adulte
(6, 2, 3, 120000.00);   -- VIP Enfant

-- =================================
-- 8️⃣ TRAJETS
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
-- 9️⃣ VOYAGES (sans colonne prix)
-- =================================
INSERT INTO voyage (id, date_heure, id_voiture, id_trajet, dure_voyage) VALUES
(1, '2026-01-15', 1, 1, 6);  -- Antananarivo → Toamasina

UPDATE voyage SET date_heure = '2026-01-17 08:00:00' WHERE id = 1;

-- =================================
-- 🔟 RÉSERVATIONS (avec id_categorie)
-- =================================
INSERT INTO reservation (id, nomClient, nbPlaces, idVoyage, idStatus, type_place, id_categorie) VALUES
(1, 'Rakoto Jean', 2, 1, 1, 1, 1),      -- Standard Adulte
(2, 'Rabe Paul', 3, 1, 1, 2, 1),        -- Premium Adulte
(3, 'Ranaivo Lala', 1, 1, 1, 1, 2);     -- Standard Enfant