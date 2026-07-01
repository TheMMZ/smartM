package com.smartmaintain.identityservice.services;

import org.springframework.stereotype.Service;

@Service
public class ClientService {


    public void demanderMaintenance(String clientId, String avionReference) {
        System.out.println("Demande de maintenance enregistrée pour le client ID : "
                + clientId + " concernant l'avion : " + avionReference);
    }

    public boolean consulterDisponibilite(String avionImmatriculation) {
        System.out.println("Consultation de la disponibilité de l'avion : " + avionImmatriculation);
        return true;
    }
}