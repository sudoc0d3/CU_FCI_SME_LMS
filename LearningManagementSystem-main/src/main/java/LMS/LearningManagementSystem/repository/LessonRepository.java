package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
     Optional<Lesson> findById(Long id);
}
