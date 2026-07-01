package com.smartmaintain.identityservice.services;

import com.smartmaintain.identityservice.clients.EquipementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private EquipementClient equipementClient;



    public List<Object> getAllMaintenances() {
        return equipementClient.getAllMaintenances();
    }

    public Object validerMaintenance(Long id) {
        return equipementClient.validateMaintenance(id);
    }

    public Object rejeterMaintenance(Long id) {
        return equipementClient.rejectMaintenance(id);
    }

    public String suiviStatutMaintenance(Long maintenanceId) {
        Object maintenance = equipementClient.getMaintenanceById(maintenanceId);
        return "Statut actuel de la maintenance : " + maintenance.toString();
    }



    public void creerEquipe(Object equipe) {
        equipementClient.saveEquipe(equipe);
        System.out.println("Équipe créée avec succès.");
    }

    public List<Object> listerEquipes() {
        return equipementClient.getAllEquipes();
    }

    public void supprimerEquipe(Long equipeId) {
        equipementClient.deleteEquipe(equipeId);
    }



    public void ajouterTache(Object tache) {
        equipementClient.saveTache(tache);
    }

    public List<Object> consulterTaches() {
        return equipementClient.getAllTaches();
    }

    public void assignerTachesAuxEquipes(Long equipeId, Long tacheId) {
        equipementClient.assignTaskToTeam(equipeId, tacheId);
    }


    public void envoyerListeAvionsMaintenanceClient(Long clientId) {

        System.out.println("Notification : Liste des avions envoyée au client ID " + clientId);
    }

    public String verifierDisponibiliteMateriaux(Long materielId) {
        return "Matériel ID " + materielId + " est disponible en stock.";
    }
}