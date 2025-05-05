package LMS.LearningManagementSystem.controller;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.AssignmentLogRepository;
import LMS.LearningManagementSystem.repository.AssignmentRepository;
import LMS.LearningManagementSystem.repository.CourseRepository;
import LMS.LearningManagementSystem.repository.StudentRepository;
import LMS.LearningManagementSystem.service.AttendanceService;
//import LMS.LearningManagementSystem.service.QuizService;
import LMS.LearningManagementSystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import LMS.LearningManagementSystem.service.NotificationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path= "/students")
public class StudentController {
    protected final StudentService studentService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AssignmentLogRepository assignmentLogRepository;
    private final NotificationService notificationService;
    private final AssignmentRepository assignmentRepository;
    //private final QuizService quizService ;

    @Autowired
    public StudentController(StudentService studentService, StudentRepository studentRepository, CourseRepository courseRepository, AssignmentLogRepository assignmentLogRepository, NotificationService notificationService, AssignmentRepository assignmentRepository /*QuizService quizService*/) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.assignmentLogRepository = assignmentLogRepository;
        this.notificationService = notificationService;
        this.assignmentRepository = assignmentRepository;
        //this.quizService=quizService;
    }


    @GetMapping(path = "/get-students")
    public ResponseEntity<List<Student>> getStudents(){
        try {
            List<Student> students = studentService.getStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
        //return studentService.getStudents();
    }

    @PostMapping(path = "/add-course")
    public String addCourse(@RequestParam int studentId, @RequestParam int courseId){
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();

                student.addCourse(course);
                course.addStudent(student);
                studentRepository.save(student);

                // Send notification to the student for successful enrollment
                String successMessageForStudent = "Enrollment successful for course: " + course.getCourseTitle();
                notificationService.createNotification(student.getId(), Role.Student, successMessageForStudent);

                // Send notification to the instructor
                Instructor instructor = course.getInstructor();
                String successMessageForInstructor = "A student (" + student.getName() + ") has enrolled in your course: " + course.getCourseTitle();
                notificationService.createNotification(instructor.getId(), Role.Instructor, successMessageForInstructor);

                return successMessageForStudent;
            } else {
                // Send notification to the student for failure (course not found)
                String failureMessage = "Enrollment failed: Course not found.";
                notificationService.createNotification(studentId, Role.Student, failureMessage);

                return failureMessage;
            }
        } else {
            // Send notification for failure (student not found)
            String failureMessage = "Enrollment failed: Student not found.";
            notificationService.createNotification(studentId,Role.Student, failureMessage);

            return failureMessage;
        }

    }
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/attend-lesson/{lessonId}")
    public ResponseEntity<String> attendLesson(@PathVariable Long lessonId, @RequestParam String otp, @RequestParam int studentId) {
        try {
            if (otp == null ) {
                return ResponseEntity.badRequest().body("Invalid OTP provided.");
            }

            boolean isAttended = attendanceService.markAttendance(lessonId, otp, studentId);
            return ResponseEntity.ok(isAttended ? "Attendance marked successfully." : "Invalid OTP. Attendance not marked.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking attendance: " + e.getMessage());
        }
        //boolean isAttended = attendanceService.markAttendance(lessonId, otp, studentId);
        //return isAttended ? "Attendance marked successfully." : "Invalid OTP. Attendance not marked.";
    }

    @PostMapping("/submit-assignment")
    public ResponseEntity<String> submitAssignment(@RequestBody AssignmentLog assignmentLog) {
        try{
        if (!assignmentRepository.findById(assignmentLog.getAssignment().getId()).isPresent()) {
            //return "Assignment not found.";
            return ResponseEntity.badRequest().body("Assignment not found.");
        }
        if (!studentRepository.findById(assignmentLog.getStudentId()).isPresent()) {
            //return "Student not found.";
            return ResponseEntity.badRequest().body("Student not found.");
        }
        assignmentLogRepository.save(assignmentLog);
        return ResponseEntity.ok("Assignment submitted successfully.");
        //return "Assignment submitted successfully.";
    }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting assignment: " + e.getMessage());
        }
}

    @PostMapping("/take-quiz")
    public ResponseEntity<String> takeQuiz(
            @RequestParam Integer quizId,
            @RequestParam Integer studentId,
            @RequestBody List<String> answers) {
        try {
            // Ensure the questions for the quiz are randomized for each attempt
            String message = studentService.takeQuiz(studentId, quizId, answers);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-quiz")
    public ResponseEntity<String> getQuiz(
            @RequestParam Integer quizId,
            @RequestParam Integer studentId) {
        try {
            // Retrieve the quiz with randomized questions
            String quiz = studentService.getQuizWithRandomQuestions(quizId, studentId);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/notifications/All")
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam int studentId) {
        try {
            //return notificationService.getAllNotifications(studentId, Role.Student);
            List<Notification> notifications = notificationService.getAllNotifications(studentId, Role.Student);
            return ResponseEntity.ok(notifications);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    @GetMapping("/notifications/unread")
    public ResponseEntity<List<Notification>> getUnReadNotifications(@RequestParam int studentId) {
        //return notificationService.getUnReadNotifications(studentId,Role.Student);
        try {
            List<Notification> notifications = notificationService.getUnReadNotifications(studentId, Role.Student);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    @PostMapping("/notifications/mark-as-read")
    public String markNotificationAsRead(@RequestParam Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return "Notification marked as read.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
    /*@PostMapping("/submit-quiz")
    public ResponseEntity<String> submitQuiz(@RequestBody QuizSubmition submission) {
        try {
            QuizLog quizLog = quizService.gradeQuiz(submission);
            return ResponseEntity.ok("Quiz submitted successfully. Your grade is: " + quizLog.getGrade() + "\nFeedback: \n" + quizLog.getFeedback());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the quiz submission: " + e.getMessage());
        }
    }*/

}