package LMS.LearningManagementSystem.configuration;

import LMS.LearningManagementSystem.model.Student;
import LMS.LearningManagementSystem.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

import java.util.List;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args -> {};
    }
}
