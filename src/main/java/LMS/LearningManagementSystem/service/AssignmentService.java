package LMS.LearningManagementSystem.service;


import LMS.LearningManagementSystem.model.Assignment;
import LMS.LearningManagementSystem.model.AssignmentLog;
import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Role;
import LMS.LearningManagementSystem.repository.AssignmentRepository;
import LMS.LearningManagementSystem.repository.AssignmentLogRepository;
import LMS.LearningManagementSystem.repository.CourseRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
    protected final CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;
    private NotificationService notificationService;
    protected final AssignmentLogRepository assignmentLogRepository;

    public AssignmentService(AssignmentLogRepository assignmentLogRepository, CourseRepository courseRepository , NotificationService notificationService , AssignmentRepository assignmentRepository) {
        this.assignmentLogRepository = assignmentLogRepository;
        this.courseRepository = courseRepository;
        this.notificationService = notificationService;
        this.assignmentRepository = assignmentRepository;
    }

    public Assignment addAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public void editAssignmentGrade(Integer assignmentId, Integer grade) {
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        assignment.setGrade(grade);
        assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Integer assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    public void submitAssignment(String token, Integer assignmentId, String answeredPdf) {
        // Extract student ID from token (pseudo code)
        Integer studentId = extractStudentIdFromToken(token);

        // Create an assignment log
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow();
        AssignmentLog log = new AssignmentLog();
        log.setStudentId(studentId);
        log.setAnsweredPdfPath(answeredPdf);
        log.setAssignment(assignment);
        assignmentLogRepository.save(log);
    }

    public void create_assignment(Assignment assignment, int instructorId) throws Exception {
        if(courseRepository.findById(assignment.getCourseId()).get().getInstructor().getId() != instructorId)
            throw new Exception("Instructor doesn't have permission");
        else
            assignmentRepository.save(assignment);
    }

    public List<Integer> track_assignmentSubmissions(int assignmentId, int instructorId) throws Exception {
        if(courseRepository.findById(assignmentRepository.findById(assignmentId).get().getCourseId()).get().getInstructor().getId() != instructorId) {
            throw new Exception("not permission");
        }
        List<AssignmentLog> assignmentLogs = assignmentLogRepository.findAll().stream()
                .filter(log -> log.getAssignment().getId().equals(assignmentId))
                .toList();

        // Return the list of grades
        return assignmentLogs.stream().map(AssignmentLog::getGrade) // Assuming AssignmentLog has a getGrade() method
                .collect(Collectors.toList());

    }

    public List<AssignmentLog> viewAllAssignmentLogs() {
        return assignmentLogRepository.findAll();
    }

    public List<AssignmentLog> viewAllAssignmentLogsForSpecificAssignment(Integer assignmentId) {
        return assignmentLogRepository.findAll().stream()
                .filter(log -> log.getAssignment().getId().equals(assignmentId))
                .toList();
    }

    public List<AssignmentLog> viewUncorrectedAssignmentLogs() {
        return assignmentLogRepository.findAll().stream()
                .filter(log -> log.getGrade() == null)
                .toList();
    }

    public List<AssignmentLog> viewUncorrectedAssignmentLogsForSpecificAssignment(Integer assignmentId) {
        return assignmentLogRepository.findAll().stream()
                .filter(log -> log.getGrade() == null && log.getAssignment().getId().equals(assignmentId))
                .toList();
    }

    public void correctAssignmentLog(Integer assignmentLogId, Integer grade) {
        AssignmentLog log = assignmentLogRepository.findById(assignmentLogId).orElseThrow();
        Course crs=courseRepository.findById(log.getAssignment().getCourseId()).orElseThrow();
        log.setGrade(grade);
        assignmentLogRepository.save(log);
        // Notify the student about the graded assignment
        String message = "Your assignment for course with ID : " + crs.getCourseTitle() +
                " has been graded. Your grade is: " + grade + ".";
        notificationService.createNotification(log.getStudentId(), Role.STUDENT, message);
    }

    private Integer extractStudentIdFromToken(String token) {
        // Placeholder function
        return 1;
    }
}