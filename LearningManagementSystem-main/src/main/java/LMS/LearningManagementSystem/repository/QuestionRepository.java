package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.Question;
import LMS.LearningManagementSystem.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByCourseId(Integer courseId);
}
