import uuid
import random

def get_uuid():
    return str(uuid.uuid4())

sql = []
sql.append("DELETE FROM public.sub_task;")
sql.append("DELETE FROM public.piece_request;")
sql.append("DELETE FROM public.tache;")
sql.append("DELETE FROM public.maintenance;")
sql.append("DELETE FROM public.equipe_technicien_names;")
sql.append("DELETE FROM public.equipe_techniciens;")
sql.append("DELETE FROM public.equipe;")
sql.append("DELETE FROM public.utilisateurs;")
sql.append("DELETE FROM public.piece;")
sql.append("DELETE FROM public.equipement;")
sql.append("DELETE FROM public.taxonomie;")

sql.append("INSERT INTO public.taxonomie (id, code, description, nom) VALUES (1, 'HVAC-001', 'Heating, Ventilation, and Air Conditioning', 'HVAC');")
sql.append("INSERT INTO public.taxonomie (id, code, description, nom) VALUES (2, 'ELEC-001', 'Electrical Systems', 'Electrical');")
sql.append("INSERT INTO public.taxonomie (id, code, description, nom) VALUES (3, 'MECH-001', 'Mechanical Equipment', 'Mechanical');")
sql.append("SELECT setval('taxonomie_id_seq', 3, true);")

password_hash = "'$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIvi'"
statuses = ["ACTIVE", "ACTIVE", "ACTIVE", "PENDING_APPROVAL"]

admins = []
for i in range(3):
    uid = get_uuid()
    admins.append(uid)
    sql.append(f"INSERT INTO public.utilisateurs (id, role, account_status, email, mot_de_passe, nom, prenom, departement) VALUES ('{uid}', 'ADMIN', 'ACTIVE', 'admin{i+1}@smartm.com', {password_hash}, 'Admin{i+1}', 'Super', 'IT');")

managers = []
for i in range(6):
    uid = get_uuid()
    managers.append(uid)
    sql.append(f"INSERT INTO public.utilisateurs (id, role, account_status, email, mot_de_passe, nom, prenom, departement) VALUES ('{uid}', 'MANAGER', 'ACTIVE', 'manager{i+1}@smartm.com', {password_hash}, 'Manager{i+1}', 'Test', 'Operations');")

engineers = []
for i in range(10):
    uid = get_uuid()
    name = f"Ingenieur {i+1}"
    engineers.append((uid, name))
    sql.append(f"INSERT INTO public.utilisateurs (id, role, account_status, email, mot_de_passe, nom, prenom, matricule_technique) VALUES ('{uid}', 'INGENIEUR', 'ACTIVE', 'ingenieur{i+1}@smartm.com', {password_hash}, '{name}', 'Test', 'ENG-{i+1:03}');")

operators = []
for i in range(20):
    uid = get_uuid()
    name = f"Operateur {i+1}"
    operators.append((uid, name))
    sql.append(f"INSERT INTO public.utilisateurs (id, role, account_status, email, mot_de_passe, nom, prenom, matricule_technique) VALUES ('{uid}', 'OPERATEUR', 'ACTIVE', 'operateur{i+1}@smartm.com', {password_hash}, '{name}', 'Test', 'TECH-{i+1:03}');")

# Create 5 teams
teams = []
for i in range(5):
    team_id = get_uuid()
    leader = engineers[i]
    team_ops = operators[i*4 : (i+1)*4] # 4 operators per team
    sql.append(f"INSERT INTO public.equipe (id, leader_engineer_id, leader_engineer_name, nom, specialite) VALUES ('{team_id}', '{leader[0]}', '{leader[1]}', 'Equipe {i+1}', 'Maintenance Spec {i+1}');")
    teams.append(team_id)
    
    for op in team_ops:
        sql.append(f"INSERT INTO public.equipe_techniciens (equipe_id, technician_id) VALUES ('{team_id}', '{op[0]}');")
        sql.append(f"INSERT INTO public.equipe_technicien_names (equipe_id, technician_name) VALUES ('{team_id}', '{op[1]}');")

equipements = []
for i in range(15):
    eq_id = i + 1
    equipements.append(eq_id)
    tax_id = random.choice([1, 2, 3])
    status = random.choice(["OPERATIONAL", "IN_MAINTENANCE", "CRITICAL"])
    sql.append(f"INSERT INTO public.equipement (id, model, nom, reference, status, taxonomie_id) VALUES ({eq_id}, 'Model-{i}', 'Equipement {i}', 'EQ-{i:03}', '{status}', {tax_id});")
sql.append("SELECT setval('equipement_id_seq', 15, true);")

pieces = []
for i in range(20):
    piece_id = get_uuid()
    pieces.append(piece_id)
    tax_id = random.choice([1, 2, 3])
    sql.append(f"INSERT INTO public.piece (id, emplacement, nom, prix, quantite, reference, seuil_min, taxonomie_id) VALUES ('{piece_id}', 'Store {i%5}', 'Piece {i}', {round(random.uniform(10.0, 500.0), 2)}, {random.randint(10, 200)}, 'P-{i:04}', {random.randint(5, 20)}, {tax_id});")

maintenances = []
for i in range(20):
    maint_id = get_uuid()
    maintenances.append(maint_id)
    eq = random.choice(equipements)
    team = random.choice(teams)
    status = random.choice(["PENDING", "IN_PROGRESS", "COMPLETED"])
    type_m = random.choice(["PREVENTIVE", "CORRECTIVE"])
    sql.append(f"INSERT INTO public.maintenance (id, date_debut, date_fin, description, status, type_maintenance, equipe_id, equipement_id) VALUES ('{maint_id}', '2026-06-01', '2026-06-30', 'Maintenance Task {i}', '{status}', '{type_m}', '{team}', {eq});")

tasks = []
for i in range(30):
    task_id = get_uuid()
    tasks.append(task_id)
    maint_id = random.choice(maintenances)
    team = random.choice(teams)
    tax_id = random.choice([1, 2, 3])
    status = random.choice(["TODO", "IN_PROGRESS", "DONE"])
    priorite = random.choice(["LOW", "MEDIUM", "HIGH", "CRITICAL"])
    sql.append(f"INSERT INTO public.tache (id, description, priorite, status, total_cost, equipe_id, maintenance_id, taxonomie_id) VALUES ('{task_id}', 'Tache detailed {i}', '{priorite}', '{status}', 0, '{team}', '{maint_id}', {tax_id});")

for i in range(60):
    sub_id = get_uuid()
    task = random.choice(tasks)
    op = random.choice(operators)
    status = random.choice(["TODO", "IN_PROGRESS", "DONE"])
    sql.append(f"INSERT INTO public.sub_task (id, assigned_member_id, assigned_member_name, description, status, tache_id) VALUES ('{sub_id}', '{op[0]}', '{op[1]}', 'Subtask {i} details', '{status}', '{task}');")

with open("c:/Users/rachi/OneDrive/Desktop/SmartM/mock_data_bulk.sql", "w", encoding="utf-8") as f:
    f.write("\n".join(sql))
