package LMS.LearningManagementSystem.service;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AssignmentLogRepository assignmentLogRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizLogRepository quizLogRepository;


    public byte[] generateExcelReport(int courseId) throws IOException {
        List<Student> students = studentRepository.findAll(); // Fetch students for the course
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Performance");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Student ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Total Grades");
        headerRow.createCell(3).setCellValue("Attendance");

        int rowNum = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getName());

            // Calculate total grades and attendance
            int totalGrades = calculateTotalGrades(student.getId(), courseId);
            int attendanceCount = calculateAttendance(student.getId(), courseId);

            row.createCell(2).setCellValue(totalGrades);
            row.createCell(3).setCellValue(attendanceCount);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    public int calculateTotalGrades(int studentId, int courseId) {
        // Ensure course assignments are retrieved
        List<Assignment> assignments = assignmentRepository.findAllByCourseId(courseId);
        List<Quiz> quizzes = quizRepository.findAllByCourseId(courseId);

        // Handle cases where there are no assignments or quizzes
        if ((assignments == null || assignments.isEmpty()) && (quizzes == null || quizzes.isEmpty())) {
            return 0; // No assignments or quizzes found
        }

        // Retrieve assignment and quiz IDs for the course
        List<Integer> assignmentIds = assignments != null ?
                assignments.stream().map(Assignment::getId).toList() :
                Collections.emptyList();

        List<Integer> quizIds = quizzes != null ?
                quizzes.stream().map(Quiz::getId).toList() :
                Collections.emptyList();

        // Retrieve assignment logs for the student
        List<AssignmentLog> assignmentLogs = assignmentLogRepository.findAllByStudentIdAndAssignmentIdIn(studentId, assignmentIds);
        List<QuizLog> quizLogs = quizLogRepository.findAllByStudentIdAndQuizIdIn(studentId, quizIds);

        // Calculate total grades for assignments and quizzes
        int assignmentTotalGrade = (assignmentLogs != null && !assignmentLogs.isEmpty()) ?
                assignmentLogs.stream().mapToInt(AssignmentLog::getGrade).sum() : 0;

        int quizTotalGrade = (quizLogs != null && !quizLogs.isEmpty()) ?
                quizLogs.stream().mapToInt(QuizLog::getScore).sum() : 0;

        // Return the combined total grade
        return assignmentTotalGrade + quizTotalGrade;
    }



    public int calculateAttendance(int studentId, int courseId) {
        // Logic to calculate attendance for the student in the course
        return (int) attendanceRepository.findAll().stream()
                .filter(attendance -> attendance.getStudent().getId() == studentId && attendance.getLesson().getCourse().getId() == courseId)
                .filter(Attendance::isAttended)
                .count();
    }

    public byte[] generatePerformanceChart(int courseId) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Student> students = studentRepository.findAll();

        for (Student student : students) {
            int totalGrades = calculateTotalGrades(student.getId(), courseId);
            dataset.addValue(totalGrades, "Grades", student.getName());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Student Performance",
                "Students",
                "Total Grades",
                dataset
        );

        ByteArrayOutputStream chartOutputStream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(chartOutputStream, chart, 800, 600);
        return chartOutputStream.toByteArray();
    }
}