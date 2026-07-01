package com.smartmaintain.identityservice.controllers;

import com.smartmaintain.identityservice.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/{clientId}/demander-maintenance/{avionReference}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> demanderMaintenance(
            @PathVariable UUID clientId,
            @PathVariable String avionReference) {

        clientService.demanderMaintenance(clientId.toString(), avionReference);
        return ResponseEntity.ok("La demande de maintenance a été enregistrée avec succès.");
    }


    @GetMapping("/disponibilite/{avionImmatriculation}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Boolean> consulterDisponibilite(@PathVariable String avionImmatriculation) {
        boolean isAvailable = clientService.consulterDisponibilite(avionImmatriculation);
        return ResponseEntity.ok(isAvailable);
    }
}