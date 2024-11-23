package com.example.Keycloak_User_Management_Service.responses;

public class UserDetailsResponse {
    private String userDetails;
    private byte[] image;

    public UserDetailsResponse(String userDetails, byte[] image) {
        this.userDetails = userDetails;
        this.image = image;
    }

    // Getters and setters
    public String getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}