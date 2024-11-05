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

@Service
public class KeycloakClientService {

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

    // Get All Users
    public ResponseEntity<String> getAllUsers(String accessToken) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Create User
    public ResponseEntity<String> createUser(String accessToken, Map<String, Object> userPayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return ResponseEntity.ok("User created successfully.");
    }

    // Update User
    public ResponseEntity<String> updateUser(String accessToken, String userId, Map<String, Object> userPayload) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
         restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
         return ResponseEntity.ok("User with ID " + userId + " updated successfully.");
    }

    // Delete User
    public ResponseEntity<String> deleteUser(String accessToken, String userId) {
        String url = keycloakBaseUrl + "/admin/realms/" + realm + "/users/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        return ResponseEntity.ok("User with ID " + userId + " deleted successfully.");
    }
}
