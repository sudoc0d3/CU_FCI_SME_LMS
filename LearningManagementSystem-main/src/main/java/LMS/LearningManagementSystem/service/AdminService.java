package LMS.LearningManagementSystem.service;

import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Role;
import LMS.LearningManagementSystem.model.Student;
import LMS.LearningManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private StudentRepository studentRepository;

    @Autowired
    public AdminService(StudentRepository studentRepository)
    {
        this.studentRepository = studentRepository;
    }

    public void addStudent(String name, String email, String password) {
        Student newStudent = new Student (name, email, password, Role.Student);
        studentRepository.save(newStudent); // Save the student to the database
        System.out.println("Student Added Successfully");
    }

    public void addCourseToStudent(Student student, Course course) {
        student.addCourse(course);
    }

    public void removeCourseFromStudent(Student student, Course course) {
        student.removeCourse(course);
    }
}