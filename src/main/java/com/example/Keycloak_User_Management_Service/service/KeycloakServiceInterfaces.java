package com.example.Keycloak_User_Management_Service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

//    ResponseEntity<String> createClientRole(String accessToken, String clientUuid, String roleName, String description);

    //    /**
    //     * Creates a client role in Keycloak for a given client UUID.
    //     *
    //     * @param accessToken The access token with admin permissions
    //     * @param clientUuid The UUID of the client
    //     * @param roleName The name of the role to create
    //     * @param description The description of the role
    //     * @return ResponseEntity<String> with the result of the role creation
    //     */

    ResponseEntity<String> createClientRole(String accessToken, String clientUuid, Map<String, Object> userPayload);

    ResponseEntity<String> updateClientRole(String accessToken, String clientUuid, String roleName, Map<String, Object> rolePayload);

    ResponseEntity<String> deleteClientRole(String accessToken, String clientUuid, String roleName);

//    ResponseEntity<?> getUserDetailsWithImage(String userId, String accessToken) throws JsonProcessingException;
}
