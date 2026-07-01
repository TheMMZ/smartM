package com.smartmaintain.identityservice.entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Utilisateur{

    public Admin(UUID id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse);
    }

    public Admin() {

    }

}
