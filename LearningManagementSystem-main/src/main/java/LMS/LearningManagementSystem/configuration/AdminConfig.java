package LMS.LearningManagementSystem.configuration;

import LMS.LearningManagementSystem.model.Admin;
import LMS.LearningManagementSystem.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AdminConfig {
    @Bean (name = "adminCommandLineRunner")
    CommandLineRunner commandLineRunner(AdminRepository repository){
        return args -> {};
    }
}