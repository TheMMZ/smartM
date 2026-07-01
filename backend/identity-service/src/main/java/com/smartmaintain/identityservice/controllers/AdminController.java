package com.smartmaintain.identityservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.identityservice.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {


    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/equipements")
    public ResponseEntity<Object> ajouterEquipement(@Valid @RequestBody Object equipement) {
        return ResponseEntity.ok(adminService.ajouterEquipement(equipement));
    }


    @PutMapping("/equipements/{id}")
    public ResponseEntity<Object> modifierEquipement(@PathVariable Long id, @Valid @RequestBody Object equipement) {
        return ResponseEntity.ok(adminService.modifierEquipement(id, equipement));
    }


    @DeleteMapping("/equipements/{id}")
    public ResponseEntity<Void> supprimerEquipement(@PathVariable Long id) {
        adminService.supprimerEquipement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/equipements/avions")
    public ResponseEntity<List<Object>> consulterListeAvions() {
        return ResponseEntity.ok(adminService.consulterListeAvions());
    }

    @GetMapping("/equipements/{id}/historique")
    public ResponseEntity<Object> consulterHistorique(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.consulterHistoriqueMaintenance(id));
    }

    @GetMapping("/supervision/statistiques")
    public ResponseEntity<String> getStats() {
        return ResponseEntity.ok(adminService.visualisationStatistiques());
    }

    @GetMapping("/supervision/alertes")
    public ResponseEntity<String> getAlertes() {
        return ResponseEntity.ok(adminService.gestionAlertes());
    }
}
