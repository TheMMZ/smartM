package com.smartmaintain.identityservice.controllers;

import com.smartmaintain.identityservice.services.IngenieurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingenieurs")
@PreAuthorize("hasAuthority('INGENIEUR') or hasAuthority('ADMIN')")
public class IngenieurController {

    @Autowired
    private IngenieurService ingenieurService;

    @GetMapping("/engines")
    public ResponseEntity<List<Object>> getAllEngines() {
        return ResponseEntity.ok(ingenieurService.getAllEngines());
    }

    @PostMapping("/analyser/{engineId}")
    public ResponseEntity<String> analyserPanne(@PathVariable Long engineId) {
        String result = ingenieurService.analyserFailure(engineId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/equipe/{id}")
    public ResponseEntity<String> consulterEquipe(@PathVariable Long id) {
        return ResponseEntity.ok(ingenieurService.consulterEquipe(id));
    }

    @PostMapping("/tache/{tacheId}/operateur/{operateurId}")
    public ResponseEntity<Void> assignerTacheOperateur(@PathVariable Long tacheId, @PathVariable Long operateurId) {
        ingenieurService.assignerTacheOperateur(tacheId, operateurId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/maintenance/{id}/fiche")
    public ResponseEntity<Void> mettreAJourFicheMaintenance(@PathVariable Long id) {
        ingenieurService.mettreAJourFicheMaintenance(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/materiaux/demander/{materiau}")
    public ResponseEntity<Void> demanderMateriaux(@PathVariable String materiau) {
        ingenieurService.demanderMateriaux(materiau);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/historique-rapports")
    public ResponseEntity<List<String>> consulterHistoriqueRapports() {
        return ResponseEntity.ok(ingenieurService.consulterHistoriqueRapports());
    }

    @PostMapping("/taches/reception")
    public ResponseEntity<Void> receptionTachesAssignees() {
        ingenieurService.receptionTachesAssignees();
        return ResponseEntity.ok().build();
    }
}