package LMS.LearningManagementSystem.configuration;

import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Instructor;
import LMS.LearningManagementSystem.model.Lesson;
import LMS.LearningManagementSystem.model.Role;
import LMS.LearningManagementSystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LessonConfig {

    // Example for a repository configuration if necessary
    @Bean (name = "lessonCommandLineRunner")
    CommandLineRunner commandLineRunner(LessonRepository repository, CourseRepository courseRepository,InstructorRepository instructorRepository){
        return args -> {};
    }
}
