-- Disable foreign key checks for clean insertions if needed, but we'll insert in order.
-- 1. TAXONOMIE
INSERT INTO public.taxonomie (id, code, description, nom) VALUES (1, 'HVAC-001', 'Heating, Ventilation, and Air Conditioning', 'HVAC');
INSERT INTO public.taxonomie (id, code, description, nom) VALUES (2, 'ELEC-001', 'Electrical Systems', 'Electrical');
INSERT INTO public.taxonomie (id, code, description, nom) VALUES (3, 'MECH-001', 'Mechanical Equipment', 'Mechanical');

-- 2. UTILISATEURS (Passwords are BCrypt of "password123")
INSERT INTO public.utilisateurs (id, role, account_status, email, mot_de_passe, nom, prenom, ice, nom_societe, siege_social, specialite, departement, matricule_technique)
VALUES 
('11111111-1111-1111-1111-111111111111', 'ADMIN', 'ACTIVE', 'admin@smartm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIvi', 'Admin', 'Super', NULL, NULL, NULL, NULL, 'IT', NULL),
('22222222-2222-2222-2222-222222222222', 'MANAGER', 'ACTIVE', 'manager@smartm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIvi', 'Manager', 'Test', NULL, NULL, NULL, NULL, 'Operations', NULL),
('33333333-3333-3333-3333-333333333333', 'INGENIEUR', 'ACTIVE', 'ingenieur@smartm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIvi', 'Ingenieur', 'Test', NULL, NULL, NULL, 'Mechanics', NULL, 'ENG-001'),
('44444444-4444-4444-4444-444444444444', 'OPERATEUR', 'ACTIVE', 'operateur@smartm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIvi', 'Operateur', 'Test', NULL, NULL, NULL, 'Electrical', NULL, 'TECH-001'),
('55555555-5555-5555-5555-555555555555', 'CLIENT', 'ACTIVE', 'client@smartm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIvi', 'Client', 'Test', '123456789', 'Smart Solutions', 'Casablanca', NULL, NULL, NULL);

-- 3. EQUIPE
INSERT INTO public.equipe (id, leader_engineer_id, leader_engineer_name, nom, specialite) 
VALUES ('66666666-6666-6666-6666-666666666666', '33333333-3333-3333-3333-333333333333', 'Ingenieur Test', 'Equipe Alpha', 'Maintenance Generale');

-- 4. EQUIPEMENT
-- Fix serial sequence so future inserts don't collide.
SELECT setval('equipement_id_seq', (SELECT MAX(id) FROM equipement), true);
INSERT INTO public.equipement (id, model, nom, reference, status, taxonomie_id) VALUES (10, 'Model-X', 'Chiller Pump 1', 'EQ-CHILL-001', 'OPERATIONAL', 1);
INSERT INTO public.equipement (id, model, nom, reference, status, taxonomie_id) VALUES (11, 'Model-Y', 'Main Generator', 'EQ-GEN-001', 'IN_MAINTENANCE', 2);
INSERT INTO public.equipement (id, model, nom, reference, status, taxonomie_id) VALUES (12, 'Model-Z', 'Conveyor Belt A', 'EQ-CONV-001', 'CRITICAL', 3);

-- 5. PIECE
INSERT INTO public.piece (id, emplacement, nom, prix, quantite, reference, seuil_min, taxonomie_id) 
VALUES ('77777777-7777-7777-7777-777777777777', 'Store A', 'Air Filter', 25.50, 100, 'P-AF-001', 10, 1);
INSERT INTO public.piece (id, emplacement, nom, prix, quantite, reference, seuil_min, taxonomie_id) 
VALUES ('88888888-8888-8888-8888-888888888888', 'Store B', 'Fuse 30A', 5.00, 200, 'P-F30-001', 50, 2);

-- 6. MAINTENANCE
INSERT INTO public.maintenance (id, date_debut, date_fin, description, status, type_maintenance, equipe_id, equipement_id) 
VALUES ('99999999-9999-9999-9999-999999999999', '2026-06-25', '2026-06-30', 'Routine check of Chiller Pump', 'IN_PROGRESS', 'PREVENTIVE', '66666666-6666-6666-6666-666666666666', 10);
INSERT INTO public.maintenance (id, date_debut, date_fin, description, status, type_maintenance, equipe_id, equipement_id) 
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2026-06-26', NULL, 'Fix generator failure', 'PENDING', 'CORRECTIVE', '66666666-6666-6666-6666-666666666666', 11);

-- 7. TACHE
INSERT INTO public.tache (id, checked_at, description, priorite, status, technician_note, total_cost, equipe_id, maintenance_id, taxonomie_id) 
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', NULL, 'Replace air filters', 'HIGH', 'TODO', NULL, 0, '66666666-6666-6666-6666-666666666666', '99999999-9999-9999-9999-999999999999', 1);
