package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Instructor;
import LMS.LearningManagementSystem.model.Student;
import LMS.LearningManagementSystem.repository.CourseRepository;
import LMS.LearningManagementSystem.repository.InstructorRepository;
import LMS.LearningManagementSystem.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class courseServiceTest {


    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCoursesByInstructor() {
        Instructor instructor = new Instructor();
        instructor.setId(1);
        List<Course> courses = Arrays.asList(new Course(), new Course());

        when(instructorRepository.findById(1)).thenReturn(Optional.of(instructor));
        when(courseRepository.findByInstructor(instructor)).thenReturn(courses);

        List<Course> result = courseService.getCoursesByInstructor(1);

        assertEquals(2, result.size());
        verify(instructorRepository, times(1)).findById(1);
        verify(courseRepository, times(1)).findByInstructor(instructor);
    }

    @Test
    void testGetCoursesByInstructor_NotFound() {
        when(instructorRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.getCoursesByInstructor(1);
        });

        assertEquals("Instructor not found with id: 1", exception.getMessage());
        verify(instructorRepository, times(1)).findById(1);
    }

    @Test
    void testGetEnrolledStudents() {
        Course course = new Course();
        List<Student> students = Arrays.asList(new Student(), new Student());
        course.setEnrolledStudents(students);

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        List<Student> result = courseService.getEnrolledStudents(1);

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findById(1);
    }

    @Test
    void testGetEnrolledStudents_CourseNotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.getEnrolledStudents(1);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository, times(1)).findById(1);
    }

    @Test
    void testAddMediaToCourse() {
        Course course = new Course();
        List<String> mediaUrls = Arrays.asList("url1", "url2");
        course.setMediaFiles(new ArrayList<>());

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.addMediaToCourse(1, mediaUrls);

        assertEquals(2, result.getMediaFiles().size());
        assertTrue(result.getMediaFiles().containsAll(mediaUrls));
        verify(courseRepository, times(1)).findById(1);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testAddMediaToCourse_CourseNotFound() {
        List<String> mediaUrls = Arrays.asList("url1", "url2");

        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            courseService.addMediaToCourse(1, mediaUrls);
        });

        assertEquals("Course not found with ID: 1", exception.getMessage());
        verify(courseRepository, times(1)).findById(1);
    }
}
