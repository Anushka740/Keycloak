package com.example.Keycloak_User_Management_Service.responses;

import com.cogno.quizapp.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
        private String message;
        private Boolean success;
        private QuestionDto question;

}
