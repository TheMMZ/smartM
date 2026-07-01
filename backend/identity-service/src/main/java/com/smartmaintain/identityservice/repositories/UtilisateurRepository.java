package com.smartmaintain.identityservice.repositories;

import com.smartmaintain.identityservice.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ضروري
import org.springframework.data.repository.query.Param; // ضروري
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByEmail(String email);

    @Query(value = "SELECT * FROM utilisateurs WHERE role = :type", nativeQuery = true)
    List<Utilisateur> findByRole(@Param("type") String type);
}