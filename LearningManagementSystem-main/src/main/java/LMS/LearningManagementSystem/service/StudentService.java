package LMS.LearningManagementSystem.service;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.QuestionRepository;
import LMS.LearningManagementSystem.repository.QuizLogRepository;
import LMS.LearningManagementSystem.repository.QuizRepository;
import LMS.LearningManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final QuizRepository quizRepository;
    private final QuizLogRepository quizLogRepository;
    private final NotificationService notificationService;
    private final QuestionRepository questionRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, QuizRepository quizRepository, QuizLogRepository quizLogRepository, NotificationService notificationService, QuestionRepository questionRepository) {
        this.studentRepository = studentRepository;
        this.quizRepository = quizRepository;
        this.quizLogRepository = quizLogRepository;
        this.notificationService = notificationService;
        this.questionRepository = questionRepository;
    }

    public List<Student> getStudents(){ // مفروض ترجع من الDatabase
        return studentRepository.findAll();
    }

    public Quiz getQuiz(Integer quizId, Integer studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new IllegalArgumentException("Quiz not found."));

        return quiz;
    }

    public String takeQuiz(Integer studentId, Integer quizId, List<String> answers) {
        // Retrieve student and quiz
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Retrieve the existing QuizLog
        QuizLog attempt = quizLogRepository.findByStudentIdAndQuizId(studentId, quizId);
        if (attempt == null) {
            throw new IllegalArgumentException("No quiz attempt found for the student and quiz.");
        }

        List<Question> questions = attempt.getQuestions();
        if (answers.size() != questions.size()) {
            throw new IllegalArgumentException("Mismatch between number of answers and questions.");
        }

        // Calculate the score
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).isCorrect(answers.get(i))) {
                score++;
            }
        }

        // Update and save the QuizLog
        attempt.setScore(score);
        attempt.setStudentAnswers(answers);
        attempt.setAttemptDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        String feedback = "Your score in " + quiz.getTitle() + " is " + score + " out of " + quiz.getTotalGrade() +
                " in " + quiz.getCourse().getCourseTitle();
        notificationService.createNotification(studentId, Role.Student, feedback);
        attempt.setFeedback(feedback);
        quizLogRepository.save(attempt);

        return feedback;
    }


    public String getQuizWithRandomQuestions(Integer quizId, Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Check if a QuizLog already exists for this student and quiz
        QuizLog existingLog = quizLogRepository.findByStudentIdAndQuizId(studentId, quizId);
        if (existingLog != null) {
            // If a log exists, return the questions from the existing log
            return formatQuiz(existingLog.getQuestions(), quiz.getTitle());
        }

        // Fetch all questions for the course
        List<Question> questions = questionRepository.findByCourseId(quiz.getCourse().getId());
        if (questions.size() < quiz.getNumberOfQuestions()) {
            throw new IllegalArgumentException("Not enough questions available for the quiz.");
        }

        // Randomize and select questions
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.subList(0, quiz.getNumberOfQuestions());

        // Create and save a new QuizLog
        QuizLog quizLog = new QuizLog();
        quizLog.setQuiz(quiz);
        quizLog.setStudent(student);
        quizLog.setQuestions(selectedQuestions);
        quizLogRepository.save(quizLog);

        // Format and return the quiz
        return formatQuiz(selectedQuestions, quiz.getTitle());
    }

    // Helper method to format quiz questions for display
    private String formatQuiz(List<Question> questions, String quizTitle) {
        StringBuilder result = new StringBuilder(quizTitle + "\n");
        for (Question question : questions) {
            result.append("\n").append(question.getTitle());
            if (!question.getOptions().isEmpty()) {
                result.append("\n").append(question.getOptions());
            }
            result.append("\n");
        }
        return result.toString();
    }



}