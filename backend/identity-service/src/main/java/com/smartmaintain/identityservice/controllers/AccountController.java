package com.smartmaintain.identityservice.controllers;

import jakarta.validation.Valid;

import com.smartmaintain.identityservice.dto.LoginRequest;
import com.smartmaintain.identityservice.dto.LoginResponse;
import com.smartmaintain.identityservice.entities.*;
import com.smartmaintain.identityservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Login attempt for email={}", request.email());
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            String role = auth.getAuthorities().iterator().next().getAuthority();

            JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .subject(auth.getName())
                    .claim("role", role)
                    .expiresAt(Instant.now().plusSeconds(36000))
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
            return ResponseEntity.ok(new LoginResponse(token, role));

        } catch (Exception e) {
            log.warn("Authentication failed for {}: {}", request.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }



    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Utilisateur> listUsers() {
        return accountService.getAllUsers();
    }
    @GetMapping("/users/role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Utilisateur> getUsersByRole(@PathVariable String role) {
        return accountService.getUsersByRole(role);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utilisateur editUser(@PathVariable UUID id, @Valid @RequestBody Utilisateur userDetails) {
        return accountService.updateUser(id, userDetails);
    }

    @PutMapping("/users/{id}/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utilisateur updateUserStatus(@PathVariable UUID id, @PathVariable String status) {
        return accountService.updateUserStatus(id, status);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable UUID id) {
        accountService.deleteUser(id);
    }



    @PostMapping("/admin")
    public Admin addAdmin(@Valid @RequestBody Admin admin) {
        return accountService.saveAdmin(admin);
    }

    @PostMapping("/client")
    public Client addClient(@Valid @RequestBody Client client) {
        return accountService.saveClient(client);
    }

    @PostMapping("/manager")
    public Manager addManager(@Valid @RequestBody Manager manager) {
        return accountService.saveManager(manager);
    }

    @PostMapping("/ingenieur")
    public Ingenieur addIngenieur(@Valid @RequestBody Ingenieur ingenieur) {
        return accountService.saveIngenieur(ingenieur);
    }

    @PostMapping("/operateur")
    public Operateur addOperateur(@Valid @RequestBody Operateur operateur) {
        return accountService.saveOperateur(operateur);
    }
}
