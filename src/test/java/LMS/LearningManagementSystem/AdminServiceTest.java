package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Student;
import LMS.LearningManagementSystem.repository.StudentRepository;
import LMS.LearningManagementSystem.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new AdminService(studentRepository);
    }

    @Test
    void testAddStudent() {
        int id = 1;
        String name = "John Doe";
        String email = "johndoe@example.com";
        String password = "password123";

        adminService.addStudent(name, email, password);

        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testAddCourseToStudent() {
        Student student = mock(Student.class);
        Course course = mock(Course.class);

        adminService.addCourseToStudent(student, course);

        verify(student, times(1)).addCourse(course);
    }

    @Test
    void testRemoveCourseFromStudent() {
        Student student = mock(Student.class);
        Course course = mock(Course.class);

        adminService.removeCourseFromStudent(student, course);

        verify(student, times(1)).removeCourse(course);
    }
}
