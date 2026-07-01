package com.smartmaintain.identityservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.identityservice.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managers")
@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
public class ManagerController {

    @Autowired
    private ManagerService managerService;


    @GetMapping("/maintenances")
    public ResponseEntity<List<Object>> getAllMaintenances() {
        return ResponseEntity.ok(managerService.getAllMaintenances());
    }

    @PutMapping("/valider-maintenance/{id}")
    public ResponseEntity<Object> validerMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.validerMaintenance(id));
    }

    @PutMapping("/rejeter-maintenance/{id}")
    public ResponseEntity<Object> rejeterMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.rejeterMaintenance(id));
    }

    @GetMapping("/maintenances/statut/{id}")
    public ResponseEntity<String> suiviStatutMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.suiviStatutMaintenance(id));
    }


    @GetMapping("/equipes")
    public ResponseEntity<List<Object>> listerEquipes() {
        return ResponseEntity.ok(managerService.listerEquipes());
    }

    @PostMapping("/equipes")
    public ResponseEntity<Void> creerEquipe(@Valid @RequestBody Object equipe) {
        managerService.creerEquipe(equipe);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/equipes/{id}")
    public ResponseEntity<Void> supprimerEquipe(@PathVariable Long id) {
        managerService.supprimerEquipe(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/equipes/{equipeId}/taches/{tacheId}")
    public ResponseEntity<Void> assignerTachesAuxEquipes(@PathVariable Long equipeId, @PathVariable Long tacheId) {
        managerService.assignerTachesAuxEquipes(equipeId, tacheId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/taches")
    public ResponseEntity<List<Object>> consulterTaches() {
        return ResponseEntity.ok(managerService.consulterTaches());
    }

    @PostMapping("/taches")
    public ResponseEntity<Void> ajouterTache(@Valid @RequestBody Object tache) {
        managerService.ajouterTache(tache);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/materiaux/disponibilite/{id}")
    public ResponseEntity<String> verifierDisponibiliteMateriaux(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.verifierDisponibiliteMateriaux(id));
    }

    @PostMapping("/client/{clientId}/avions/liste")
    public ResponseEntity<Void> envoyerListeAvionsMaintenanceClient(@PathVariable Long clientId) {
        managerService.envoyerListeAvionsMaintenanceClient(clientId);
        return ResponseEntity.ok().build();
    }
}
