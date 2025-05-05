package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    Optional<Instructor> findById(int id);

    Optional<Instructor> findByEmail(String email);
}