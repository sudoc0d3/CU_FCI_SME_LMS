package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.AssignmentLogRepository;
import LMS.LearningManagementSystem.repository.AssignmentRepository;
import LMS.LearningManagementSystem.repository.CourseRepository;
import LMS.LearningManagementSystem.service.AssignmentService;
import LMS.LearningManagementSystem.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private AssignmentLogRepository assignmentLogRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    void testAddAssignment() {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setCourseId(1);
        when(assignmentRepository.save(assignment)).thenReturn(assignment);

        Assignment savedAssignment = assignmentService.addAssignment(assignment);

        // Assert
        assertNotNull(savedAssignment);
        assertEquals(1, savedAssignment.getCourseId());
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void testEditAssignmentGrade() {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setGrade(0);

        when(assignmentRepository.findById(1)).thenReturn(Optional.of(assignment));

        assignmentService.editAssignmentGrade(1, 85);

        // Assert
        assertEquals(85, assignment.getGrade());
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void testDeleteAssignment() {
        assignmentService.deleteAssignment(1);

        // Assert
        //assertEquals("Optional.empty",assignmentRepository.findById(1));
        verify(assignmentRepository, times(1)).deleteById(1);
    }

    @Test
    void testSubmitAssignment() {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setId(1);

        when(assignmentRepository.findById(1)).thenReturn(Optional.of(assignment));
        assignmentService.submitAssignment("Token", 1, "path/to/pdf");

        // Assert
        // Verify that an AssignmentLog was created and saved with the correct PDF path
        ArgumentCaptor<AssignmentLog> logCaptor = ArgumentCaptor.forClass(AssignmentLog.class);
        verify(assignmentLogRepository, times(1)).save(logCaptor.capture());

        // Get the captured AssignmentLog and assert the PDF path
        AssignmentLog capturedLog = logCaptor.getValue();
        assertEquals("path/to/pdf", capturedLog.getAnsweredPdfPath());
    }

    @Test
    void testCreateAssignment() throws Exception {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setCourseId(1);

        Course course = new Course();
        course.setId(1);
        Instructor instructor = new Instructor();
        instructor.setId(1);
        course.setInstructor(instructor);

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        assignmentService.create_assignment(assignment, 1);

        // Assert
        assertEquals(1,assignment.getCourseId());
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void testTrackAssignmentSubmissions() throws Exception {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setCourseId(1);

        Course course = new Course();
        course.setId(1);
        Instructor instructor = new Instructor();
        instructor.setId(1);
        course.setInstructor(instructor);

        AssignmentLog log1 = new AssignmentLog();
        log1.setId(1);
        log1.setAssignment(assignment);
        log1.setGrade(85);

        AssignmentLog log2 = new AssignmentLog();
        log2.setId(2);
        log2.setAssignment(assignment);
        log2.setGrade(90);

        when(assignmentRepository.findById(1)).thenReturn(Optional.of(assignment));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(assignmentLogRepository.findAll()).thenReturn(List.of(log1, log2));

        List<Integer> grades = assignmentService.track_assignmentSubmissions(1, 1);

        // Assert
        assertEquals(2, grades.size());
        assertTrue(grades.contains(85));
        assertTrue(grades.contains(90));
    }

    @Test
    void testCorrectAssignmentLog() {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setId(1);
        assignment.setCourseId(1);

        AssignmentLog log = new AssignmentLog();
        log.setId(1);
        log.setStudentId(2026);
        log.setAssignment(assignment);

        Course course = new Course();
        course.setId(1);
        course.setCourseTitle("Sample Course");

        when(assignmentLogRepository.findById(1)).thenReturn(Optional.of(log));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        assignmentService.correctAssignmentLog(1, 95);

        // Assert
        assertEquals(95, log.getGrade());
        verify(assignmentLogRepository, times(1)).save(log);
        verify(notificationService, times(1))
                .createNotification(eq(2026), eq(Role.Student), contains("Your grade is: 95"));
    }
}
