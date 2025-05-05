package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.*;
import LMS.LearningManagementSystem.service.NotificationService;
import LMS.LearningManagementSystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizLogRepository quizLogRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Quiz quiz;
    private QuizLog quizLog;
    private Question question;
    private List<Question> questions;

    private static final Integer STUDENT_ID = 1;
    private static final Integer QUIZ_ID = 101;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock student and quiz data
        student = new Student();
        student.setId(STUDENT_ID);
        student.setName("Test Student");

        // Create a course for the quiz
        Course course = new Course();
        course.setId(1);
        course.setCourseTitle("Sample Course");

        // Initialize quiz with course
        quiz = new Quiz();
        quiz.setId(QUIZ_ID);
        quiz.setTitle("Sample Quiz");
        quiz.setNumberOfQuestions(3);
        quiz.setTotalGrade(5);
        quiz.setCourse(course); // Set the course to quiz

        // Initialize the question and set correct answer
        question = new Question();
        question.setTitle("Sample Question");
        question.setOptions(Arrays.asList("A", "B", "C"));
        question.setCorrectAnswer("A"); // Set the correct answer to avoid NullPointerException

        questions = Arrays.asList(question, question, question);

        // Create a quiz log
        quizLog = new QuizLog();
        quizLog.setStudent(student);
        quizLog.setQuiz(quiz);
        quizLog.setQuestions(questions);
    }



    @Test
    void testGetStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getStudents();

        assertEquals(1, result.size(), "The student list should contain one student");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetQuiz() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(quiz));

        Quiz result = studentService.getQuiz(QUIZ_ID, STUDENT_ID);

        assertNotNull(result, "Quiz should not be null");
        assertEquals(quiz.getId(), result.getId(), "Quiz IDs should match");
    }

    @Test
    void testGetQuiz_studentNotFound() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.getQuiz(QUIZ_ID, STUDENT_ID);
        });
    }

    @Test
    void testGetQuiz_quizNotFound() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.getQuiz(QUIZ_ID, STUDENT_ID);
        });
    }

    @Test
    void testTakeQuiz_validInput() {
        List<String> answers = Arrays.asList("A", "B", "C");

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(quiz));
        when(quizLogRepository.findByStudentIdAndQuizId(STUDENT_ID, QUIZ_ID)).thenReturn(quizLog);
        when(questionRepository.findByCourseId(quiz.getCourse().getId())).thenReturn(questions);

        String feedback = studentService.takeQuiz(STUDENT_ID, QUIZ_ID, answers);

        assertNotNull(feedback, "Feedback should not be null");
        assertTrue(feedback.contains("Your score"), "Feedback should contain score");
        verify(quizLogRepository, times(1)).save(quizLog);
        verify(notificationService, times(1)).createNotification(anyInt(), eq(Role.Student), anyString());
    }

    @Test
    void testTakeQuiz_noQuizAttempt() {
        List<String> answers = Arrays.asList("A", "B", "C");

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(quiz));
        when(quizLogRepository.findByStudentIdAndQuizId(STUDENT_ID, QUIZ_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.takeQuiz(STUDENT_ID, QUIZ_ID, answers);
        });
    }

    @Test
    void testGetQuizWithRandomQuestions_existingLog() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(quiz));
        when(quizLogRepository.findByStudentIdAndQuizId(STUDENT_ID, QUIZ_ID)).thenReturn(quizLog);

        String result = studentService.getQuizWithRandomQuestions(QUIZ_ID, STUDENT_ID);

        assertNotNull(result, "Quiz result should not be null");
        assertTrue(result.contains(quiz.getTitle()), "Quiz title should be present in the result");
    }

    @Test
    void testGetQuizWithRandomQuestions_noLog() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(quiz));
        when(quizLogRepository.findByStudentIdAndQuizId(STUDENT_ID, QUIZ_ID)).thenReturn(null);
        when(questionRepository.findByCourseId(quiz.getCourse().getId())).thenReturn(questions);

        String result = studentService.getQuizWithRandomQuestions(QUIZ_ID, STUDENT_ID);

        assertNotNull(result, "Quiz result should not be null");
        assertTrue(result.contains(quiz.getTitle()), "Quiz title should be present in the result");
        verify(quizLogRepository, times(1)).save(any(QuizLog.class));
    }

    @Test
    void testGetQuizWithRandomQuestions_notEnoughQuestions() {
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(quizRepository.findById(QUIZ_ID)).thenReturn(Optional.of(quiz));
        when(questionRepository.findByCourseId(quiz.getCourse().getId())).thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.getQuizWithRandomQuestions(QUIZ_ID, STUDENT_ID);
        });
    }
}
