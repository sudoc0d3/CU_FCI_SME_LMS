package LMS.LearningManagementSystem.configuration;

import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Instructor;
import LMS.LearningManagementSystem.model.Role;
import LMS.LearningManagementSystem.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import LMS.LearningManagementSystem.repository.InstructorRepository;

import java.util.List;

@Configuration
public class CourseConfig {
    @Bean (name = "courseCommandLineRunner")
    CommandLineRunner commandLineRunner(CourseRepository repository){
        return args -> { };
    }
}