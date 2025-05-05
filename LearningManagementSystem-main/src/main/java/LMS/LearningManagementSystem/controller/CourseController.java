package LMS.LearningManagementSystem.controller;

import LMS.LearningManagementSystem.model.Course;
import LMS.LearningManagementSystem.model.Lesson;
import LMS.LearningManagementSystem.model.Student;
// import LMS.LearningManagementSystem.model.Instructor;
// import LMS.LearningManagementSystem.repository.CourseRepository;
// import LMS.LearningManagementSystem.repository.InsturctorRepository;
// import LMS.LearningManagementSystem.repository.*;
import LMS.LearningManagementSystem.repository.LessonRepository;
import LMS.LearningManagementSystem.service.CourseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import LMS.LearningManagementSystem.service.FileUploadService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/course")

public class CourseController {
    private final CourseService courseService;
    private final FileUploadService fileUploadService;

    @Autowired
    public CourseController(CourseService courseService, FileUploadService fileUploadService){
        this.courseService=courseService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/allCourses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/instructorCourses")
    public List<Course> getCoursesByInstructor(@RequestParam int instructorId) {
        return this.courseService.getCoursesByInstructor(instructorId);
    }

    @GetMapping("/enrolledStudents")
    public List<Student> getEnrolledStudents(@RequestParam int courseId) {
        return this.courseService.getEnrolledStudents(courseId);
    }
    @PostMapping("/uploadMedia")
    public ResponseEntity<Course> uploadMedia(
            @RequestParam int courseId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> mediaUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                String fileUrl = fileUploadService.uploadFile(file);
                mediaUrls.add(fileUrl);
            }

            Course updatedCourse = courseService.addMediaToCourse(courseId, mediaUrls);
            return ResponseEntity.ok(updatedCourse);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}