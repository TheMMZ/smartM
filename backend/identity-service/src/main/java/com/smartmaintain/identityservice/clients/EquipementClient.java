package com.smartmaintain.identityservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "equipement-service")
public interface EquipementClient {

    @PostMapping("/api/equipements")
    Object saveEquipement(@RequestBody Object equipement);

    @GetMapping("/api/equipements/avions")
    List<Object> getAvions();

    @GetMapping("/api/equipements/{id}/historique")
    String getHistorique(@PathVariable("id") Long id);

    @PutMapping("/api/equipements/{id}")
    Object updateEquipement(@PathVariable("id") Long id, @RequestBody Object equipement);

    @DeleteMapping("/api/equipements/{id}")
    void deleteEquipement(@PathVariable("id") Long id);

    @GetMapping("/api/equipements/engines")
    List<Object> getAllEngines();

    @GetMapping("/api/equipements/{id}")
    Object getEquipementById(@PathVariable("id") Long id);

    @PutMapping("/api/equipements/{id}/fiche-maintenance")
    void updateMaintenanceFiche(@PathVariable("id") Long id);

    // Maintenances
    @GetMapping("/api/maintenances")
    List<Object> getAllMaintenances();

    @GetMapping("/api/maintenances/{id}")
    Object getMaintenanceById(@PathVariable("id") Long id);

    @PutMapping("/api/maintenances/{id}/validate")
    Object validateMaintenance(@PathVariable("id") Long id);

    @PutMapping("/api/maintenances/{id}/reject")
    Object rejectMaintenance(@PathVariable("id") Long id);

    // Equipes
    @PostMapping("/api/equipes")
    Object saveEquipe(@RequestBody Object equipe);

    @GetMapping("/api/equipes")
    List<Object> getAllEquipes();

    @DeleteMapping("/api/equipes/{id}")
    void deleteEquipe(@PathVariable("id") Long id);

    // Taches
    @PostMapping("/api/taches")
    Object saveTache(@RequestBody Object tache);

    @GetMapping("/api/taches")
    List<Object> getAllTaches();

    @PostMapping("/api/equipes/{equipeId}/tasks/{tacheId}")
    void assignTaskToTeam(@PathVariable("equipeId") Long equipeId, @PathVariable("tacheId") Long tacheId);


}
