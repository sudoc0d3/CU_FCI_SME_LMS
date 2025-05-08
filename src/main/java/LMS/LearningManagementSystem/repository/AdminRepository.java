package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.Admin;
import LMS.LearningManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
}