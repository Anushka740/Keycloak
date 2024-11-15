package com.example.Keycloak_User_Management_Service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
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

    @Value("${keycloak.clientUuid}")
    private String clientUuid;

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
     * Retrieves roles for a given Keycloak client UUID.
     *
     * @param accessToken The access token with admin permissions
     * @param clientUuid The UUID of the client
     * @return ResponseEntity<String> with the list of client roles in JSON format
     */
    public ResponseEntity<String> getClientRoles(String accessToken, String clientUuid) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    /**
     * Retrieves the ID of a client role by its name.
     *
     * @param accessToken The access token with admin permissions.
     * @param clientUuid  The UUID of the client associated with the role.
     * @param roleName    The name of the role.
     * @return The ID of the role.
     */
    private String getClientRoleId(String accessToken, String clientUuid, String roleName) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        for (Map<String, Object> role : response.getBody()) {
            if (roleName.equals(role.get("name"))) {
                return (String) role.get("id");
            }
        }

        throw new RuntimeException("Role " + roleName + " not found for client " + clientUuid);
    }
    /**
     * Creates a client role in Keycloak for a specified client UUID.
     *
     * @param accessToken The access token with sufficient permissions (admin) to interact with the Keycloak Admin API.
     * @param clientUuid  The UUID of the client for which the role is to be created.
     * @param userPayload A map containing role details.
     * @return A ResponseEntity with a success message if the role is created successfully, or an error message if the creation fails.
     */
    @Override
    public ResponseEntity<String> createClientRole(String accessToken, String clientUuid, Map<String, Object> userPayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return ResponseEntity.ok("Role created successfully.");
    }

    /**
     * Updates an existing client role by role name in Keycloak for a specified client UUID.
     *
     * @param accessToken The access token with sufficient permissions to interact with the Keycloak Admin API.
     * @param clientUuid  The UUID of the client for which the role is to be updated.
     * @param roleName    The name of the role to be updated.
     * @param rolePayload A map containing updated role details.
     * @return A ResponseEntity with a success message if the role is updated successfully, or an error message if the update fails.
     */
    @Override
    public ResponseEntity<String> updateClientRole(String accessToken, String clientUuid, String roleName, Map<String, Object> rolePayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles/" + roleName;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(rolePayload, headers);
        restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        return ResponseEntity.ok("Client role updated successfully.");
    }

    /**
     * Deletes a client role by role name in Keycloak for a specified client UUID.
     *
     * @param accessToken The access token with sufficient permissions to interact with the Keycloak Admin API.
     * @param clientUuid  The UUID of the client for which the role is to be deleted.
     * @param roleName    The name of the role to be deleted.
     * @return A ResponseEntity with a success message if the role is deleted successfully, or an error message if the deletion fails.
     */
    @Override
    public ResponseEntity<String> deleteClientRole(String accessToken, String clientUuid, String roleName) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles/" + roleName;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        return ResponseEntity.ok("Client role deleted successfully.");
    }

    /**
     * Creates a new realm role in Keycloak.
     *
     * @param accessToken The access token with admin permissions to interact with the Keycloak API.
     * @param rolePayload A map containing role details, typically including:
     *                    <ul>
     *                        <li>name - The name of the role.</li>
     *                        <li>description - A brief description of the role.</li>
     *                        <li>composite - Boolean indicating if the role is composite.</li>
     *                    </ul>
     * @return A ResponseEntity with a success message if the role is created successfully, or an error message otherwise.
     */
    public ResponseEntity<String> createRealmRole(String accessToken, Map<String, Object> rolePayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/roles";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(rolePayload, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return ResponseEntity.ok("Realm role created successfully.");
    }

    /**
     * Retrieves all realm roles in Keycloak.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @return A ResponseEntity containing a JSON array of all realm roles.
     */
    public ResponseEntity<String> getAllRealmRoles(String accessToken) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/roles";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    /**
     * Updates an existing realm role by its name.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @param roleName    The name of the role to update.
     * @param rolePayload A map containing updated role details.
     * @return A ResponseEntity with a success message if the role is updated successfully.
     */
    public ResponseEntity<String> updateRealmRole(String accessToken, String roleName, Map<String, Object> rolePayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/roles/" + roleName;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(rolePayload, headers);
        restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        return ResponseEntity.ok("Realm role updated successfully.");
    }

    /**
     * Deletes a realm role by its name.
     *
     * @param accessToken The access token for authenticating with Keycloak.
     * @param roleName    The name of the role to delete.
     * @return A ResponseEntity with a success message if the role is deleted successfully.
     */
    public ResponseEntity<String> deleteRealmRole(String accessToken, String roleName) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/roles/" + roleName;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
        return ResponseEntity.ok("Realm role deleted successfully.");
    }

    /**
     * Assigns a client role to a user in Keycloak.
     *
     * @param accessToken The access token with admin permissions.
     * @param userId      The ID of the user to whom the role is being assigned.
     * @param clientUuid  The UUID of the client for which the role is defined.
     * @param roles       A list of roles to assign to the user (each role should include the "id" and "name").
     * @return A ResponseEntity with a success message if the role is assigned successfully.
     */
    public ResponseEntity<String> assignClientRoleToUser(
            String accessToken,
            String userId,
            String clientUuid,
            List<Map<String, Object>> roles) {

        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + clientUuid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(roles, headers);

        restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return ResponseEntity.ok("Client role assigned to user successfully.");
    }

    /**
     * Deletes a client role assigned to a user in Keycloak using the role name.
     *
     * @param accessToken The access token with admin permissions.
     * @param userId      The ID of the user from whom the role is being removed.
     * @param clientUuid  The UUID of the client for which the role is defined.
     * @param roleName    The name of the role to be removed from the user.
     * @return A ResponseEntity with a success message if the role is removed successfully.
     */
    public ResponseEntity<String> deleteUserClientRole(
            String accessToken,
            String userId,
            String clientUuid,
            String roleName) {

        // Construct the URL for the client's roles for the given user
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles";

        // Fetch the available roles for the client
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(url, HttpMethod.GET, request,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        // Find the role with the given name
        Map<String, Object> roleToRemove = response.getBody().stream()
                .filter(role -> roleName.equals(role.get("name")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        // Prepare the URL to remove the role from the user
        String roleMappingUrl = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + clientUuid;

        // Use the role ID and name in the request body to remove the role from the user
        List<Map<String, Object>> rolesToRemove = Collections.singletonList(roleToRemove);

        HttpEntity<List<Map<String, Object>>> deleteRequest = new HttpEntity<>(rolesToRemove, headers);
        restTemplate.exchange(roleMappingUrl, HttpMethod.DELETE, deleteRequest, String.class);

        return ResponseEntity.ok("Client role removed from user successfully.");
    }

    /**
     * Assigns a list of realm roles to a user in Keycloak.
     *
     * @param accessToken The access token with admin permissions.
     * @param userId      The user ID.
     * @param roles       A list of realm roles to assign (each role should include "id" and "name").
     */
    public void assignRealmRolesToUser(String accessToken, String userId, List<Map<String, Object>> roles) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(roles, headers);

        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    /**
     * Deletes a specific realm role assigned to a user in Keycloak by role name.
     *
     * @param accessToken The access token with admin permissions.
     * @param userId      The user ID.
     * @param roleName    The name of the realm role to remove.
     */
    public void deleteAssignedRealmRoleFromUser(String accessToken, String userId, String roleName) {
        String rolesUrl = keycloakBaseUrl + "/admin/realms/" + realm + "/roles/" + roleName;
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        // Retrieve the role details (ID and Name) for the roleName
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                rolesUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> roleDetails = response.getBody();
        if (roleDetails == null || !roleDetails.containsKey("id")) {
            throw new RuntimeException("Role not found: " + roleName);
        }

        // Remove the retrieved role from the user
        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(
                List.of(roleDetails),
                headers
        );

        restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
    }


}
