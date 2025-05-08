package LMS.LearningManagementSystem;


import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.AssignmentLogRepository;
import LMS.LearningManagementSystem.repository.AssignmentRepository;
import LMS.LearningManagementSystem.repository.AttendanceRepository;
import LMS.LearningManagementSystem.repository.StudentRepository;
import LMS.LearningManagementSystem.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AssignmentLogRepository assignmentLogRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private ReportService reportService;

    private Student student;
    private Assignment assignment;
    private AssignmentLog assignmentLog;
    private Attendance attendance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        student = new Student();
        student.setId(1);
        student.setName("John Doe");

        Course course = new Course();
        course.setId(1);

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setCourse(course); // Associate Lesson with Course

        attendance = new Attendance();
        attendance.setAttended(true);
        attendance.setStudent(student);
        attendance.setLesson(lesson); // Associate Attendance with Lesson

        assignment = new Assignment();
        assignment.setId(1); // Set an ID for the assignment
        assignment.setCourseId(1);

        assignmentLog = new AssignmentLog();
        assignmentLog.setGrade(90);
        assignmentLog.setStudentId(student.getId());
        assignmentLog.setAssignment(assignment);
    }

    @Test
    void testGenerateExcelReport() throws IOException {
        // Arrange
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);
        when(assignmentRepository.findAllByCourseId(1)).thenReturn(Arrays.asList(assignment));
        when(assignmentLogRepository.findAllByStudentIdAndAssignmentIdIn(student.getId(), Arrays.asList(assignment.getId())))
                .thenReturn(Arrays.asList(assignmentLog));
        when(attendanceRepository.findAll()).thenReturn(Arrays.asList(attendance));

        byte[] report = reportService.generateExcelReport(1);

        // Assert
        assertNotNull(report);
        assertTrue(report.length > 0);

        // Verify that all necessary methods were called
        verify(studentRepository, times(1)).findAll();
        verify(assignmentRepository, times(1)).findAllByCourseId(1);
        verify(assignmentLogRepository, times(1)).findAllByStudentIdAndAssignmentIdIn(student.getId(), Arrays.asList(assignment.getId()));
        verify(attendanceRepository, times(1)).findAll();
    }

    @Test
    void testGeneratePerformanceChart() throws IOException {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);
        when(assignmentRepository.findAllByCourseId(1)).thenReturn(Arrays.asList(assignment));
        when(assignmentLogRepository.findAllByStudentIdAndAssignmentIdIn(student.getId(), Arrays.asList(assignment.getId())))
                .thenReturn(Arrays.asList(assignmentLog));

        byte[] chartData = reportService.generatePerformanceChart(1);

        // Assert
        assertNotNull(chartData);
        assertTrue(chartData.length > 0);

        // Verify method calls
        verify(studentRepository, times(1)).findAll();
        verify(assignmentRepository, times(1)).findAllByCourseId(1);
        verify(assignmentLogRepository, times(1)).findAllByStudentIdAndAssignmentIdIn(student.getId(), Arrays.asList(assignment.getId()));
    }

    @Test
    void testCalculateTotalGrades() {
        // Arrange: Mock the repository to return the prepared data
        when(assignmentRepository.findAllByCourseId(1)).thenReturn(Arrays.asList(assignment)); // Return assignment for course ID 1
        when(assignmentLogRepository.findAllByStudentIdAndAssignmentIdIn(
                student.getId(), Arrays.asList(assignment.getId())))
                .thenReturn(Arrays.asList(assignmentLog)); // Return assignment logs for the student
        int totalGrades = reportService.calculateTotalGrades(student.getId(), 1);

        // Assert: Verify the result
        assertEquals(90, totalGrades); // As per the setup, the total grade should be 90
        verify(assignmentRepository, times(1)).findAllByCourseId(1);
        verify(assignmentLogRepository, times(1)).findAllByStudentIdAndAssignmentIdIn(student.getId(), Arrays.asList(assignment.getId()));

    }



    @Test
    void testCalculateAttendance() {
        // Arrange: Return fully initialized Attendance objects
        when(attendanceRepository.findAll()).thenReturn(Arrays.asList(attendance));

        int attendanceCount = reportService.calculateAttendance(student.getId(), 1);
        // Assert: Verify the attendance count
        assertEquals(1, attendanceCount); // The attendance count should be 1
    }

}
