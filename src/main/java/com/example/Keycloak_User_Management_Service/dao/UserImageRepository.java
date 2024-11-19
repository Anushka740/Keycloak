package com.example.Keycloak_User_Management_Service.dao;

import com.example.Keycloak_User_Management_Service.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUserId(String userId);
}
