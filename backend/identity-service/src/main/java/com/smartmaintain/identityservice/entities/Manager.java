package com.smartmaintain.identityservice.entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("MANAGER")
public class Manager extends Utilisateur{
    private String departement;
}
