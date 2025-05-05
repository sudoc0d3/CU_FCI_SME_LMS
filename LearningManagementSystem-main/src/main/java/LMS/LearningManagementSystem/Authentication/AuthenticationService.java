package LMS.LearningManagementSystem.Authentication;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.AdminRepository;
import LMS.LearningManagementSystem.repository.StudentRepository;
import LMS.LearningManagementSystem.repository.InstructorRepository;
import LMS.LearningManagementSystem.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final InstructorRepository instuructorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtServices;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        switch (request.getRole().toString()) {
            case "Student":
                var student = Student.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
                studentRepository.save(student);
                var studentToken = jwtServices.generateToken(student);
                return AuthenticationResponse.builder().token(studentToken).build();

            case "Admin":
                var admin = Admin.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
                adminRepository.save(admin);
                var adminToken = jwtServices.generateToken(admin);
                return AuthenticationResponse.builder().token(adminToken).build();

            case "Instructor":
                var instructor = Instructor.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
                instuructorRepository.save(instructor);
                var instructorToken = jwtServices.generateToken(instructor);
                return AuthenticationResponse.builder().token(instructorToken).build();

            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var student = studentRepository.findByEmail(request.getEmail());
        if (student.isPresent()) {
            var studentToken = jwtServices.generateToken(student.get());
            return AuthenticationResponse.builder().token(studentToken).build();
        }

        var admin = adminRepository.findByEmail(request.getEmail());
        if (admin.isPresent()) {
            var adminToken = jwtServices.generateToken(admin.get());
            return AuthenticationResponse.builder().token(adminToken).build();
        }

        var instructor = instuructorRepository.findByEmail(request.getEmail());
        if (instructor.isPresent()) {
            var instructorToken = jwtServices.generateToken(instructor.get());
            return AuthenticationResponse.builder().token(instructorToken).build();
        }

        throw new IllegalArgumentException("User not found");
    }

}