package com.smartmaintain.identityservice.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("OPERATEUR")
public class Operateur extends Utilisateur{
    private String matriculeTechnique;

    public String getMatriculeTechnique() {
        return matriculeTechnique;
    }

    public void setMatriculeTechnique(String matriculeTechnique) {
        this.matriculeTechnique = matriculeTechnique;
    }
}
