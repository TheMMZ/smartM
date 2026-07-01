package com.smartmaintain.identityservice.services;

import com.smartmaintain.identityservice.entities.*;
import com.smartmaintain.identityservice.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email makaynchii: " + email));

        if (user.getAccountStatus() != null && !"APPROVED".equals(user.getAccountStatus())) {
            throw new org.springframework.security.authentication.DisabledException("Account status is " + user.getAccountStatus() + ". Login not allowed.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getMotDePasse())
                .roles(user.getRole())
                .build();
    }

    private String resolveDefaultStatus() {
        try {
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                return "APPROVED";
            }
        } catch (Exception e) {
            // fallback
        }
        return "PENDING_APPROVAL";
    }



    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur updateUser(UUID id, Utilisateur userDetails) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getAccountStatus() != null) {
            user.setAccountStatus(userDetails.getAccountStatus());
        }

        if (userDetails.getMotDePasse() != null && !userDetails.getMotDePasse().isEmpty()) {
            user.setMotDePasse(passwordEncoder.encode(userDetails.getMotDePasse()));
        }

        return utilisateurRepository.save(user);
    }

    public void deleteUser(UUID id) {
        utilisateurRepository.deleteById(id);
    }

    public List<Utilisateur> getUsersByRole(String role) {
        return utilisateurRepository.findByRole(role);
    }

    public Utilisateur updateUserStatus(UUID id, String status) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setAccountStatus(status);
        return utilisateurRepository.save(user);
    }

    public Client saveClient(Client client) {
        client.setMotDePasse(passwordEncoder.encode(client.getMotDePasse()));
        client.setAccountStatus(resolveDefaultStatus());
        return utilisateurRepository.save(client);
    }

    public Admin saveAdmin(Admin admin) {
        admin.setMotDePasse(passwordEncoder.encode(admin.getMotDePasse()));
        admin.setAccountStatus("APPROVED");
        return utilisateurRepository.save(admin);
    }

    public Manager saveManager(Manager manager) {
        manager.setMotDePasse(passwordEncoder.encode(manager.getMotDePasse()));
        manager.setAccountStatus(resolveDefaultStatus());
        return utilisateurRepository.save(manager);
    }

    public Ingenieur saveIngenieur(Ingenieur ingenieur) {
        ingenieur.setMotDePasse(passwordEncoder.encode(ingenieur.getMotDePasse()));
        ingenieur.setAccountStatus(resolveDefaultStatus());
        return utilisateurRepository.save(ingenieur);
    }

    public Operateur saveOperateur(Operateur operateur) {
        operateur.setMotDePasse(passwordEncoder.encode(operateur.getMotDePasse()));
        operateur.setAccountStatus(resolveDefaultStatus());
        return utilisateurRepository.save(operateur);
    }
}