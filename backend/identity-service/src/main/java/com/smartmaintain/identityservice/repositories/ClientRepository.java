package com.smartmaintain.identityservice.repositories;

import com.smartmaintain.identityservice.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository  extends JpaRepository<Client, UUID> {
    Client findByIce(String ice);
}
