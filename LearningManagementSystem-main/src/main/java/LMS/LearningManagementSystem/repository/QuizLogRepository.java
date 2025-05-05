package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.AssignmentLog;
import LMS.LearningManagementSystem.model.QuizLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizLogRepository extends JpaRepository<QuizLog, Integer> {
    List<QuizLog> findAllByStudentIdAndQuizIdIn(int studentId, List<Integer> quizIds);
    QuizLog findByStudentIdAndQuizId(int studentId, int quizIds);
}
