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
(2, 'VIP');

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
(1, 'TNB-001', 15),
(2, 'TNB-002', 18),
(3, 'TNB-003', 20),
(4, 'TNB-004', 12);

-- =================================
-- 5️⃣ PLACE_VOITURE
-- =================================
INSERT INTO place_voiture (id, id_type_place, prix, id_voiture, nombre_place) VALUES
(1, 1, 5000.00, 1, 10),   -- Standard pour voiture 1
(2, 2, 10000.00, 1, 5),   -- VIP pour voiture 1
(3, 1, 5000.00, 2, 12),   -- Standard pour voiture 2
(4, 2, 10000.00, 2, 6),   -- VIP pour voiture 2
(5, 1, 5000.00, 3, 15),   -- Standard pour voiture 3
(6, 2, 10000.00, 3, 5);   -- VIP pour voiture 3


INSERT INTO place_voiture (id, id_type_place, prix, id_voiture, nombre_place) VALUES
(7, 1, 5000.00, 4, 6),   -- Standard pour voiture 4
(8, 2, 10000.00, 4, 6);   -- VIP pour voiture 4

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
(1, '2026-01-15', 30000.00, 1, 1, 6),  -- Antananarivo → Toamasina
(2, '2026-01-15', 35000.00, 2, 2, 7),  -- Antananarivo → Fianarantsoa
(3, '2026-01-16', 40000.00, 3, 3, 10), -- Antananarivo → Mahajanga
(4, '2026-01-16', 50000.00, 1, 4, 15), -- Antananarivo → Toliara
(5, '2026-01-17', 32000.00, 2, 5, 5),  -- Toamasina → Fianarantsoa
(6, '2026-01-17', 45000.00, 3, 6, 12), -- Fianarantsoa → Toliara
(7, '2026-01-18', 30000.00, 4, 7, 10), -- Mahajanga → Antananarivo
(8, '2026-01-18', 55000.00, 4, 8, 12); -- Toliara → Antananarivo

-- =================================
-- 8️⃣ RÉSERVATIONS (EXEMPLE)
-- =================================
INSERT INTO reservation (id, nomClient, nbPlaces, idVoyage, idStatus, type_place) VALUES
(1, 'Rakoto Jean', 2, 1, 1, 1),      -- Standard
(2, 'Rabe Paul', 3, 1, 1, 2),        -- VIP
(3, 'Ranaivo Lala', 1, 2, 1, 1),     -- Standard
(4, 'Andrianina Fanja', 2, 3, 1, 2); -- VIP