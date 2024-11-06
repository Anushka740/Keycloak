package com.example.Keycloak_User_Management_Service.responses;

import com.cogno.quizapp.dto.QuizDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
        private String message;
        private Boolean success;
        private QuizDto quiz;

}
