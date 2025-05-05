package LMS.LearningManagementSystem.service;

import LMS.LearningManagementSystem.model.*;
import LMS.LearningManagementSystem.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;


@Service
public class AttendanceService {

    @Autowired
    public LessonRepository lessonRepository;

    @Autowired
    public AttendanceRepository attendanceRepository;

    public boolean markAttendance(Long lessonId, String otp, int studentId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        // Check if the OTP matches
        if (lesson.getOtp().equals(otp)) {
            Attendance attendance = new Attendance();
            attendance.setLesson(lesson);

            // Use the builder to create a Student instance
            Student student = Student.builder()
                    .id(studentId)
                    .name("Ahmed") // Replace with actual name
                    .email("ahmed@gmail") // Replace with actual email
                    .password("123123") // Replace with actual password
                    .build();

            attendance.setStudent(student);

            attendance.setAttended(true);
            attendance.setTimestamp(LocalDateTime.now());
            attendanceRepository.save(attendance);
            return true;
        }
        return false; // Invalid OTP
    }
}

