package com.example.Keycloak_User_Management_Service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ImageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData {
    @Id
    private String id; // Use userId directly as the primary key

    private String name;
    private String type;

    @Lob
    @Column(name = "imagedata", length = 1000000)
    private byte[] imageData;

}