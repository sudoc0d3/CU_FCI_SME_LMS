package LMS.LearningManagementSystem.controller;

import LMS.LearningManagementSystem.service.AdminService;
import LMS.LearningManagementSystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;

    @Autowired
    AdminController(AdminService adminService, ReportService reportService){this.adminService = adminService;
        this.reportService = reportService;
    }

    @PostMapping("/add-student")
    public String addStudent(@RequestParam int id, @RequestParam String name, @RequestParam String email, @RequestParam String password) {
        adminService.addStudent(name, email, password);
        return "Student added successfully!";
    }
    @PostMapping("/add-course-to-student")
    public String addCourseToStudent(@RequestParam int studentId, @RequestParam int courseId) {
        return "Course added successfully to the student!";
    }

    @PostMapping("/remove-course-from-student")
    public String removeCourseFromStudent(@RequestParam int studentId, @RequestParam int courseId) {
        return "Course removed successfully from the student!";
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