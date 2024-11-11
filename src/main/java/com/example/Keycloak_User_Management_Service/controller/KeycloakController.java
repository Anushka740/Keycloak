package com.example.Keycloak_User_Management_Service.controller;

import com.example.Keycloak_User_Management_Service.service.KeycloakClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/keycloak")
public class KeycloakController {

    @Autowired
    private KeycloakClientService keycloakClientService;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> userPayload) {
        return keycloakClientService.createUser(token.replace("Bearer ", ""), userPayload);
    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers(@RequestHeader("Authorization") String token) {
        return keycloakClientService.getAllUsers(token.replace("Bearer ", ""));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String token, @PathVariable String userId, @RequestBody Map<String, Object> userPayload) {
        return keycloakClientService.updateUser(token.replace("Bearer ", ""), userId, userPayload);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId) {
        return keycloakClientService.deleteUser(token.replace("Bearer ", ""), userId);
    }

    // Usage example
    public void fetchClientRoles(String accessToken, String clientUuid) {
        ResponseEntity<String> response = keycloakClientService.getClientRoles(accessToken, clientUuid);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Client roles: " + response.getBody());
        } else {
            System.out.println("Failed to fetch client roles: " + response.getStatusCode());
        }
    }
}
