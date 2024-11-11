package com.example.Keycloak_User_Management_Service.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface KeycloakServiceInterfaces {
    // Get All Users
    ResponseEntity<String> getAllUsers(String accessToken);

    // Create User
    ResponseEntity<String> createUser(String accessToken, Map<String, Object> userPayload);

    // Update User
    ResponseEntity<String> updateUser(String accessToken, String userId, Map<String, Object> userPayload);

    // Delete User
    ResponseEntity<String> deleteUser(String accessToken, String userId);


    ResponseEntity<String> getClientRoles(String accessToken, String clientUuid);
}
