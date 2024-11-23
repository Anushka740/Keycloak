package com.example.Keycloak_User_Management_Service.dao;

import com.example.Keycloak_User_Management_Service.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<ImageData,String> {


    Optional<ImageData> findById(String Id);
    Optional<ImageData> findByName(String fileName);
}