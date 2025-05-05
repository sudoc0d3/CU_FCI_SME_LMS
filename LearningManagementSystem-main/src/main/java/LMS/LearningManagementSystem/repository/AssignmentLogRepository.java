package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.AssignmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentLogRepository extends JpaRepository<AssignmentLog, Integer> {
    List<AssignmentLog> findAllByStudentIdAndAssignmentIdIn(Integer studentId, List<Integer> assignmentIds);
}
