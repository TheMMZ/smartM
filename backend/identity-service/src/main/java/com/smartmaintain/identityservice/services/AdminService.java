package com.smartmaintain.identityservice.services;

import com.smartmaintain.identityservice.clients.EquipementClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private EquipementClient equipementClient;



    public Object ajouterEquipement(Object equipement) {
        return equipementClient.saveEquipement(equipement);
    }


    public Object modifierEquipement(Long id, Object equipement) {
        return equipementClient.updateEquipement(id, equipement);
    }

    // حذف
    public void supprimerEquipement(Long id) {
        equipementClient.deleteEquipement(id);
    }

    public List<Object> consulterListeAvions() {
        return equipementClient.getAvions();
    }

    public String consulterHistoriqueMaintenance(Long id) {
        return equipementClient.getHistorique(id);
    }

    public String visualisationStatistiques() {
        return "Statistiques : 92% succès, 5 pannes en cours.";
    }

    public String gestionAlertes() {
        return "Alertes actives : Aucune alerte critique.";
    }
}