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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    @PostMapping("/clients/{clientUuid}/roles")
    public ResponseEntity<String> createClientRole(
            @RequestHeader("Authorization") String token,
            @PathVariable String clientUuid,
            @RequestBody Map<String, Object> userPayload) {

        // Add the client UUID to the payload if needed
        userPayload.put("containerId", clientUuid);
        return keycloakClientService.createClientRole(token.replace("Bearer ", ""), clientUuid, userPayload);
    }


    @GetMapping("/clients/{clientUuid}/roles")
    public ResponseEntity<String> getClientRoles(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("clientUuid") String clientUuid) {

        // Remove "Bearer " prefix if present in the access token
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        return keycloakClientService.getClientRoles(accessToken, clientUuid);
    }

    @PutMapping("/clients/{clientUuid}/{roleName}")
    public ResponseEntity<String> updateClientRole(
            @RequestHeader("Authorization") String token,
            @PathVariable String clientUuid,
            @PathVariable String roleName,
            @RequestBody Map<String, Object> rolePayload) {
        return keycloakClientService.updateClientRole(token.replace("Bearer ", ""), clientUuid, roleName, rolePayload);
    }

    @DeleteMapping("/clients/{clientUuid}/{roleName}")
    public ResponseEntity<String> deleteClientRole(
            @RequestHeader("Authorization") String token,
            @PathVariable String clientUuid,
            @PathVariable String roleName) {
        return keycloakClientService.deleteClientRole(token.replace("Bearer ", ""), clientUuid, roleName);
    }

    @PostMapping("/realms/{realm}/roles")
    public ResponseEntity<String> createRealmRole(@RequestHeader("Authorization") String token,
                                                  @RequestBody Map<String, Object> rolePayload) {
        return keycloakClientService.createRealmRole(token.replace("Bearer ", ""), rolePayload);
    }

    @GetMapping("/realms/{realm}/roles")
    public ResponseEntity<String> getAllRealmRoles(@RequestHeader("Authorization") String token) {
        return keycloakClientService.getAllRealmRoles(token.replace("Bearer ", ""));
    }

    @PutMapping("/realms/{realm}/roles/{roleName}")
    public ResponseEntity<String> updateRealmRole(@RequestHeader("Authorization") String token,
                                                  @PathVariable String roleName,
                                                  @RequestBody Map<String, Object> rolePayload) {
        return keycloakClientService.updateRealmRole(token.replace("Bearer ", ""), roleName, rolePayload);
    }

    @DeleteMapping("/realms/{realm}/roles/{roleName}")
    public ResponseEntity<String> deleteRealmRole(@RequestHeader("Authorization") String token,
                                                  @PathVariable String roleName) {
        return keycloakClientService.deleteRealmRole(token.replace("Bearer ", ""), roleName);
    }

    @PostMapping("/users/{userId}/role-mappings/clients/{clientUuid}")
    public ResponseEntity<String> assignClientRoleToUser(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId,
            @PathVariable String clientUuid,
            @RequestBody List<Map<String, Object>> roles) {

        return keycloakClientService.assignClientRoleToUser(
                token.replace("Bearer ", ""),
                userId,
                clientUuid,
                roles
        );
    }

    @DeleteMapping("/users/{userId}/role-mappings/clients/{clientUuid}/roles/{roleName}")
    public ResponseEntity<String> deleteUserClientRole(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId,
            @PathVariable String clientUuid,
            @PathVariable String roleName) {

        return keycloakClientService.deleteUserClientRole(
                token.replace("Bearer ", ""),
                userId,
                clientUuid,
                roleName
        );
    }

    @PostMapping("/users/{userId}/role-mappings/realm")
    public ResponseEntity<String> assignRealmRoles(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId,
            @RequestBody List<Map<String, Object>> roles) {

        keycloakClientService.assignRealmRolesToUser(token.replace("Bearer ", ""), userId, roles);
        return ResponseEntity.ok("Realm roles assigned successfully.");
    }

    @DeleteMapping("/{realm}/users/{userId}/role-mappings/realm/{roleName}")
    public ResponseEntity<String> deleteAssignedRealmRole(
            @RequestHeader("Authorization") String token,
            @PathVariable String realm,
            @PathVariable String userId,
            @PathVariable String roleName) {

        keycloakClientService.deleteAssignedRealmRoleFromUser(token.replace("Bearer ", ""), userId, roleName);
        return ResponseEntity.ok("Assigned realm role deleted successfully.");
    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadUserImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        try {
            keycloakClientService.saveUserImage(userId, file);
            return ResponseEntity.ok("Image uploaded successfully for user: " + userId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }

}
