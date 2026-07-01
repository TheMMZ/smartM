package com.smartmaintain.identityservice.services;

import com.smartmaintain.identityservice.clients.EquipementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IngenieurService {

    @Autowired
    private EquipementClient equipementClient;



    public List<Object> getAllEngines() {
        return equipementClient.getAllEngines();
    }


    public String analyserFailure(Long engineId) {

        Map<String, Object> engine = (Map<String, Object>) equipementClient.getEquipementById(engineId);

        if (engine == null) {
            throw new RuntimeException("Moteur introuvable");
        }


        Double temp = Double.valueOf(engine.get("temperature").toString());
        Double vib = Double.valueOf(engine.get("vibration").toString());
        String ref = (String) engine.get("reference");

        if (temp > 85.0 || vib > 12.0) {
            return "Alerte : Risque de panne élevé pour le moteur " + ref + ". Remplacement recommandé.";
        }
        return "Analyse terminée pour le moteur " + ref + " : Risque de panne faible.";
    }


    public String consulterEquipe(Long equipeId) {
        return "Informations de l'équipe ID: " + equipeId;
    }

    public void assignerTacheOperateur(Long tacheId, Long operateurId) {

        System.out.println("Tâche " + tacheId + " assignée à l'opérateur " + operateurId);
    }

    public void demanderMateriaux(String materiau) {
        System.out.println("Demande de matériaux effectuée : " + materiau);
    }
    public List<String> consulterHistoriqueRapports() {
        return List.of("Rapport_Moteur_A1_Janvier.pdf", "Maintenance_Feb_2026.docx");
    }

    public void mettreAJourFicheMaintenance(Long id) {
        System.out.println("Mise à jour de la fiche de maintenance pour l'ID : " + id);
        equipementClient.updateMaintenanceFiche(id);
    }

    public void receptionTachesAssignees() {
             System.out.println("Réception des tâches assignées : 3 nouvelles tâches de maintenance détectées.");
    }


}