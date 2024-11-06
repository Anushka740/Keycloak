package com.example.Keycloak_User_Management_Service.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseResponse {
    private String message;
    private Boolean success;
    private Integer statusCode;
}
