package LMS.LearningManagementSystem.Authentication;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationRequest {
    private String email;
    private String password;
}