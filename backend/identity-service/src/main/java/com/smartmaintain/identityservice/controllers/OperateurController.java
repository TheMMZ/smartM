package com.smartmaintain.identityservice.controllers;

import com.smartmaintain.identityservice.services.OperateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operateurs")
@PreAuthorize("hasAuthority('OPERATEUR') or hasAuthority('ADMIN')")
public class OperateurController {

    @Autowired
    private OperateurService operateurService;

    @PutMapping("/stock/{pieceReference}/{quantity}")
    public ResponseEntity<String> updateStock(
            @PathVariable String pieceReference,
            @PathVariable int quantity) {
        operateurService.updateStock(pieceReference, quantity);
        return ResponseEntity.ok("Stock mis à jour avec succès.");
    }
}