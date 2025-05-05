package LMS.LearningManagementSystem.controller;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.*;
import LMS.LearningManagementSystem.service.*;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/instructor")
public class InstructorController {
    @Autowired
    protected final InstructorService instructorService;
    protected final LessonService lessonService;
    protected final AssignmentService assignmentService;
    protected final ReportService reportService;
    protected final InstructorRepository instructorRepository;
    protected final CourseRepository  courseRepository;
    protected final AttendanceRepository attendanceRepository;
    protected final LessonRepository lessonRepository;
    protected final StudentRepository studentRepository;
    protected final AssignmentRepository assignmentRepository;
    protected final AssignmentLogRepository assignmentLogRepository;
    protected final NotificationService notificationService;
    protected final QuestionRepository questionRepository;
    protected final QuizRepository quizRepository;
    protected final QuizLogRepository quizLogRepository;
    //private final QuizService quizService;

    @Autowired
    public InstructorController(AssignmentLogRepository assignmentLogRepository, InstructorService instructorService, LessonService lessonService, AssignmentService assignmentService, ReportService reportService, InstructorRepository instructorRepository, CourseRepository courseRepository, AttendanceRepository attendanceRepository, LessonRepository lessonRepository, StudentRepository studentRepository, AssignmentRepository assignmentRepository, NotificationService notificationService, QuestionRepository questionRepository, QuizRepository quizRepository, QuizLogRepository quizLogRepository /*QuizService quizService*/) {
        this.assignmentLogRepository = assignmentLogRepository;
        this.instructorService = instructorService;
        this.lessonService = lessonService;
        this.assignmentService = assignmentService;
        this.reportService = reportService;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationService = notificationService;
        //this.quizService = quizService;
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.quizLogRepository = quizLogRepository;
    }

    //@Autowired
    /*InstructorController(AssignmentLogRepository assignmentLogRepository, InstructorService instructorService, AssignmentService assignmentService, InstructorRepository instructorRepository, CourseRepository  courseRepository, AttendanceRepository attendanceRepository, LessonRepository lessonRepository, LessonService lessonService, StudentRepository studentRepository, AssignmentRepository assignmentRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.instructorService = instructorService;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.lessonRepository = lessonRepository;
        this.lessonService = lessonService;
        this.assignmentLogRepository = assignmentLogRepository;
        this.notificationService = new NotificationService();
    }*/


    @PostMapping(path = "/create-course")
    public String createCourse(@RequestParam int instructorId, @RequestParam int courseId , @RequestParam String courseTitle , @RequestParam int courseDuration )
    {
        if (courseDuration <= 0) {
            return "Invalid course Duration";
        }
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);

        if (instructorOptional.isEmpty()) {
            return "Instructor with ID not found.";
        }

        Instructor instructor = instructorOptional.get();

        Course course = new Course( courseTitle, courseDuration);
        course.setInstructor(instructor);

        courseRepository.save(course);

        return "Course created successfully and assigned to instructor.";

    }

    @PostMapping(path = "/create-assignment")
    public String createAssignment(@RequestBody Assignment assignment,@RequestParam int instructorId){
        try{
            assignmentService.create_assignment(assignment, instructorId);
            return "Assignment created successfully";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
    @DeleteMapping(path = "/delete-course")
    public String deleteCourse(@RequestParam int instructorId, @RequestParam int courseId) {
        Optional<Instructor> instructorOptional = instructorRepository.findById(instructorId);

        if (instructorOptional.isEmpty()) {
            return "Instructor with ID " + instructorId + " not found.";
        }

        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isEmpty()) {
            return "Course with ID  not found.";
        }

        try {
            courseRepository.deleteById(courseId);
            return "Course deleted successfully.";
        } catch (Exception e) {
            return "Error deleting course: " + e.getMessage();
        }
    }
    @GetMapping("/getall")
    public List<Instructor> getall(){
        return instructorRepository.findAll();
    }

    @PostMapping("/generate-otp/{lessonId}")
    public String generateOtp(@PathVariable Long lessonId) {
        try {
            lessonService.setOtpForLesson(lessonId);
            return "OTP generated for lesson ID: " + lessonId;
        } catch (Exception e) {
            return "Error generating OTP: " + e.getMessage();
        }
    }


    // Call the service to get the assignment logs for the specified assignment ID

    @GetMapping(path = "/track-student-assignment")
    public int trackAssignmentStudents(
            @RequestParam int courseid, @RequestParam int studentId) {


        // Retrieve assignments related to the given course ID
        List<Assignment> assignments = assignmentRepository.findAllByCourseId(courseid);

        if (assignments.isEmpty()) {
            throw new RuntimeException("No assignments found for the course ID.");
        }
        // Extract assignment IDs
        List<Integer> assignmentIds = assignments.stream()
                .map(Assignment::getId)
                .toList();

        // Get the student entity (assuming studentRepository is available)
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Count the number of submissions for this student for the given course
        long submissionCount = assignmentLogRepository.findAll().stream()
                .filter(log ->
                        Objects.equals(log.getStudentId(), student.getId()) &&
                                assignmentIds.contains(log.getAssignment().getId()) &&
                                log.getAnsweredPdfPath() != null) // Ensure the student has answered the assignment
                .count(); // Count the number of submissions

        // Return the count of submissions
        return (int) submissionCount;
    }


    @GetMapping(path = "/track-attendance")
    public List<Integer> track_attendance(@RequestParam int lessonId) {
        // Fetch the lesson to ensure it exists
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Retrieve all attendance records for the specified lesson
        List<Attendance> attendanceRecords = attendanceRepository.findAll().stream()
                .filter(attendance -> Objects.equals(attendance.getLesson().getId(), lesson.getId()))
                .toList();

        // Extract and return the list of student IDs who attended
        return attendanceRecords.stream()
                .filter(Attendance::isAttended) // Only include those who attended
                .map(attendance -> attendance.getStudent().getId()) // Get student ID
                .toList();
    }

    @GetMapping(path = "/track-student-attendance")
    public int trackStudentAttendance(@RequestParam int studentid, @RequestParam int courseid) {
        // Fetch the student and course, throwing an exception if not found
        Student student = studentRepository.findById(studentid)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseid)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Retrieve all lessons associated with the course
        List<Lesson> courseLessons = lessonRepository.findAll().stream()
                .filter(lesson -> Objects.equals(lesson.getCourse().getId(), course.getId()))
                .toList();

        // Retrieve all attendance records for the specific student and course lessons
        long attendedLessonCount = attendanceRepository.findAll().stream()
                .filter(attendance ->
                        Objects.equals(attendance.getStudent().getId(), student.getId()) &&
                                courseLessons.contains(attendance.getLesson()) &&
                                attendance.isAttended()) // Only include attended lessons
                .count(); // Count the number of attended lessons

        // Return the count of lessons attended
        return (int) attendedLessonCount;
    }
    /*@GetMapping(path = "/track-quizScores")
    public Map<Integer, List<Integer>> trackPerformance(@RequestParam int instructorId) {
        //return QuizService.trackInstructorQuizScores(instructorId);
        return quizService.trackInstructorQuizScores(instructorId);
    }*/
    @PostMapping(path = "/correct-assignment")
    public String correctAssignment(@RequestParam Integer assignmentLogId, @RequestParam Integer grade) {
        if (assignmentLogId == null || grade == null || grade < 0) {
            return "Invalid assignment log ID or grade.";
        }

        try {
            assignmentService.correctAssignmentLog(assignmentLogId, grade);
            return "Assignment graded successfully!";
        } catch (Exception e) {
            return "Error correcting assignment: " + e.getMessage();
        }
    }
    @GetMapping(path = "/getlessons")
    public List<Lesson> get_lessons (){

        return lessonRepository.findAll();
    }
    @GetMapping(path = "assignments")
    public List<Assignment> get_assignments(){
        return assignmentRepository.findAll();
    }
    @GetMapping(path = "assignmentlogs")
    public List<AssignmentLog> get_logs(){
        return assignmentLogRepository.findAll();
    }

    @GetMapping("/notifications/All")
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam int instructorId) {
        //return notificationService.getAllNotifications(instructorId,Role.Instructor);
        try {
            List<Notification> notifications = notificationService.getAllNotifications(instructorId, Role.Student);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
    @GetMapping("/notifications/unread")
    public ResponseEntity<List<Notification>> getUnReadNotifications(@RequestParam int instructorId) {
        //return notificationService.getUnReadNotifications(instructorId,Role.Instructor);
        try {
            List<Notification> notifications = notificationService.getUnReadNotifications(instructorId, Role.Student);
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

    @PostMapping("/create-quiz")
    public ResponseEntity<String> createQuiz(@RequestParam Integer instructorId,
                                             @RequestParam Integer courseId,
                                             @RequestParam String title,
                                             @RequestParam Integer numberOfQuestions) {
        try{
            instructorService.createQuiz(instructorId, courseId, title, numberOfQuestions);
            return ResponseEntity.ok("Quiz created.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-question")
    public ResponseEntity<String> createQuestion(@RequestParam Integer instructorId, @RequestBody Question question) {
        try {
            instructorService.createQuestion(instructorId, question);
            return ResponseEntity.ok("Question created.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "/track-quiz")
    public Map<Integer, Integer> trackQuiz(@RequestParam int quizId) {
        // Fetch the quiz to ensure it exists
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Retrieve all QuizLog records associated with the specified quiz
        List<QuizLog> quizLogs = quizLogRepository.findAll().stream()
                .filter(quizLog -> Objects.equals(quizLog.getQuiz().getId(), quiz.getId()))
                .toList();

        // Create a map of student IDs to their scores
        Map<Integer, Integer> studentGrades = new HashMap<>();
        for (QuizLog quizLog : quizLogs) {
            Integer studentId = quizLog.getStudent().getId();
            Integer score = quizLog.getScore();
            studentGrades.put(studentId, score);
        }

        return studentGrades;
    }

    @GetMapping(path = "/track-student-quiz")
    public Map<String, Integer> trackStudentQuiz(@RequestParam int studentid, @RequestParam int courseid) {
        // Fetch the student and course, throwing an exception if not found
        Student student = studentRepository.findById(studentid)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseid)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Retrieve all lessons associated with the course
        List<QuizLog> quizLogs = quizLogRepository.findAll().stream()
                .filter(quizLog -> Objects.equals(quizLog.getQuiz().getCourse().getId(), course.getId()))
                .toList();

        quizLogs.stream().filter(quizLog -> Objects.equals(quizLog.getStudent().getId(), student.getId())).toList();

        // Retrieve all attendance records for the specific student and course lessons
        Map<String, Integer> studentGrades = new HashMap<>();
        for (QuizLog quizLog : quizLogs) {
            String title = quizLog.getQuiz().getTitle();
            Integer score = quizLog.getScore();
            studentGrades.put(title, score);
        }

        return studentGrades;
    }


    @GetMapping("/questions")
    public List<Question> getQuestions(){
        return questionRepository.findAll();
    }
    @GetMapping("/quizzes")
    public List<Quiz> getQuizzes(){
        return quizRepository.findAll();
    }

    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport(@RequestParam int courseId) {
        try {
            byte[] report = reportService.generateExcelReport(courseId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=student_performance_report.xlsx")
                    .body(report);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/generate-performance-chart")
    public ResponseEntity<byte[]> generatePerformanceChart(@RequestParam int courseId) {
        try {
            byte[] chart = reportService.generatePerformanceChart(courseId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=student_performance_chart.png")
                    .body(chart);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}