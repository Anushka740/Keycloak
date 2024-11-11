package com.example.Keycloak_User_Management_Service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Service for managing users in Keycloak using its REST API.
 * This service provides methods for fetching, creating, updating, and deleting users.
 */
@Service
public class KeycloakClientService implements KeycloakServiceInterfaces {

    private final RestTemplate restTemplate;

    @Value("${keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    public KeycloakClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches all users from Keycloak.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @return A ResponseEntity containing a JSON string with the list of users.
     */
    @Override
    public ResponseEntity<String> getAllUsers(String accessToken) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    /**
     * Creates a new user in Keycloak.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @param userPayload A map representing the user details.
     * @return A ResponseEntity with a success message upon successful user creation.
     */
    @Override
    public ResponseEntity<String> createUser(String accessToken, Map<String, Object> userPayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return ResponseEntity.ok("User created successfully.");
    }

    /**
     * Updates an existing user in Keycloak.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @param userId The ID of the user to be updated.
     * @param userPayload A map containing the user details to be updated.
     * @return A ResponseEntity with a success message upon successful user update.
     */
    @Override
    public ResponseEntity<String> updateUser(String accessToken, String userId, Map<String, Object> userPayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
         restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
         return ResponseEntity.ok("User with ID " + userId + " updated successfully.");
    }

    /**
     * Deletes a user in Keycloak.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @param userId The ID of the user to be deleted.
     * @return A ResponseEntity with a success message upon successful user deletion.
     */
    @Override
    public ResponseEntity<String> deleteUser(String accessToken, String userId) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        return ResponseEntity.ok("User with ID " + userId + " deleted successfully.");
    }

    /**
     * Retrieves the roles for a specific client in a Keycloak realm.
     *
     * @param accessToken The access token for authorization.
     * @param clientUuid  The UUID of the client whose roles are to be retrieved.
     * @return A ResponseEntity containing the response body with client roles or an error message.
     */
    @Override
    public ResponseEntity<String> getClientRoles(String accessToken, String clientUuid) {
        // Construct the URL to access client roles
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles";

        // Set up the headers with the Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // Create an HTTP entity with headers only
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the GET request to fetch client roles
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
