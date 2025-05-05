# Learning Management System (LMS)

## Overview
This Java-Based Learning Management System (LMS) is a web-based application designed to streamline the management 
and organization of online courses, assessments, and performance tracking. It provides a comprehensive solution for students, instructors, 
and admins to interact with the platform effectively, fulfilling specific user needs.

## Project Structure
```
LMS_Project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │       └── LMS/
│   │   │           ├── configuration/               # Configuration classes (Database, Security, etc.)
│   │   │           │   ├── DatabaseConfig.java
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   └── SwaggerConfig.java
│   │   │           │
│   │   │           ├── controller/           # REST Controllers for handling HTTP requests
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── CourseController.java
│   │   │           │   └── StudentController.java
│   │   │           │   └── InstructorController.java
│   │   │           │   └── AdminController.java
│   │   │           │
│   │   │           ├── service/              # Business logic layer
│   │   │           │   ├── AuthService.java
│   │   │           │   ├── CourseService.java
│   │   │           │   ├── NotificationService.java
│   │   │           │   ├── AssessmentService.java
│   │   │           │   └── StudentService.java
│   │   │           │   └── InsturctorService.java
│   │   │           │   └── AdminService.java
│   │   │           │
│   │   │           ├── model/                
│   │   │           │   ├── User.java         
│   │   │           │   ├── Student.java
│   │   │           │   ├── Instructor.java
│   │   │           │   ├── Admin.java
│   │   │           │   ├── Course.java
│   │   │           │   ├── Assessment.java
│   │   │           │   ├── Quiz.java
│   │   │           │   ├── Assignment.java
│   │   │           │   └── Notification.java
│   │   │           │
│   │   │           ├── repository/           # layer for database interactions
│   │   │           │   ├── UserRepository.java
│   │   │           │   ├── CourseRepository.java
│   │   │           │   ├── AssessmentRepository.java
│   │   │           │   └── NotificationRepository.java
│   │   │           │   └── StudentRepository.java
│   │   │           │   └── InstructorRepository.java
│   │   │           │   └── AdminRepository.java
│   │   │           │
│   │   │           │
│   │   │           └── LMSApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties            
│   │       └── schema.sql                        
│   │    
│   │
│   ├── test/                                    
│   │   └── java/
│   │         └── LMS/
│   │             ├── AuthServiceTest.java
│   │             ├── UserRepositoryTest.java
│   │             └── CourseControllerTest.java
│   │
├── pom.xml                                       # Maven dependencies
└── README.md                                     
```

## Key Features
1. User Management
- Admin Features:
    - Manages system settings.
    - Creates and manages users.
    - Oversees courses.
- Instructor Features:
    - Creates and manages course content.
    - Adds assignments and quizzes.
    - Grades students and manages enrollments.
Student Features:
    - Enrolls in courses and accesses course materials.
    - Submits assignments and takes quizzes.
    - Views grades and progress.
2. Course Management
- Course Creation:
    - Instructors create courses with titles, descriptions, and media uploads (videos, PDFs, etc.).
    - Courses consist of multiple lessons for students.
- Enrollment Management:
    - Students view and enroll in courses.
    - Admins and instructors monitor enrollments.
- Attendance Management:
    - OTP-based lesson attendance for students.
3. Assessment & Grading
- Quizzes:
    - Multiple question types (MCQ, true/false, short answers).
    - Questions bank for randomized selection.
- Assignments:
    - Students upload submissions for instructor review.
- Grading:
    - Automated feedback for quizzes.
- Manual grading and feedback for assignments.
4. Performance Tracking
5. Notifications
- Student Progress:
    - Tracks quiz scores, assignments, and attendance.
    - Monitored by instructors.

## Technical Stack
- Backend: Java with Spring Boot for RESTful APIs.
- Database: MySQL, PostgreSQL, or SQLite.
- Testing: JUnit for unit testing.
- Authentication & Authorization: Spring Security.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## contributors
- Mariam Assem
- Mina Emad
- Mahmoud Ali
- Reem Ahmed
