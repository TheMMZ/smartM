package com.smartmaintain.identityservice.services;

import org.springframework.stereotype.Service;

@Service
public class OperateurService {

    public void updateStock(String pieceReference, int quantity) {
        System.out.println("Mise à jour du stock : Pièce " + pieceReference + " - Nouvelle quantité : " + quantity);
    }
}