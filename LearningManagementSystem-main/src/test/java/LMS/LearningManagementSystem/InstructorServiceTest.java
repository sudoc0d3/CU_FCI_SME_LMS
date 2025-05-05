package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.*;
import LMS.LearningManagementSystem.service.InstructorService;
import LMS.LearningManagementSystem.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    private InstructorService instructorService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentLogRepository assignmentLogRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private InstructorService service;

    private Course course;
    private Instructor instructor;
    private Question question;
    private Quiz quiz;

    private static final int INSTRUCTOR_ID = 1;
    private static final int COURSE_ID = 101;
    private static final String QUIZ_TITLE = "Sample Quiz";
    private static final int NUMBER_OF_QUESTIONS = 5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the Course and Instructor
        instructor = new Instructor();
        instructor.setId(INSTRUCTOR_ID);

        course = new Course();
        course.setId(COURSE_ID);
        course.setInstructor(instructor);
        course.setCourseTitle("Sample Course");

        question = new Question();
        question.setCourse(course);

        quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTitle(QUIZ_TITLE);
        quiz.setNumberOfQuestions(NUMBER_OF_QUESTIONS);
        quiz.setTotalGrade(NUMBER_OF_QUESTIONS);
    }

//    @Test
//    void testCreateQuiz_validInput() {
//        // Mock the repository responses
//        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
//        List<Question> questionList = Arrays.asList(new Question(), new Question(), new Question(), new Question(), new Question());
//        when(questionRepository.findByCourseId(COURSE_ID)).thenReturn(questionList);
//
//        service.createQuiz(INSTRUCTOR_ID, COURSE_ID, QUIZ_TITLE, NUMBER_OF_QUESTIONS);
//
//        // Verify interactions and results
//        verify(quizRepository, times(1)).save(any(Quiz.class));
//        verify(notificationService, times(1)).createNotification(anyInt(), eq(Role.Student), anyString());
//    }

    @Test
    void testCreateQuiz_invalidInstructor() {
        // Mock the repository responses
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        // Simulate a different instructor ID
        assertThrows(IllegalArgumentException.class, () -> {
            service.createQuiz(2, COURSE_ID, QUIZ_TITLE, NUMBER_OF_QUESTIONS);
        });
    }

    @Test
    void testCreateQuiz_courseNotFound() {
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            service.createQuiz(INSTRUCTOR_ID, COURSE_ID, QUIZ_TITLE, NUMBER_OF_QUESTIONS);
        });
    }

//    @Test
//    void testCreateQuestion_validInput() {
//        // Mock repository responses
//        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
//
//        service.createQuestion(INSTRUCTOR_ID, question);
//
//        // Verify save method is called
//        verify(questionRepository, times(1)).save(question);
//    }

    @Test
    void testCreateQuestion_invalidInstructor() {
        // Mock repository responses
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        // Simulate a different instructor ID
        assertThrows(IllegalArgumentException.class, () -> {
            service.createQuestion(2, question);
        });
    }

    @Test
    void testCreateQuestion_courseNotFound() {
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            service.createQuestion(INSTRUCTOR_ID, question);
        });
    }
}