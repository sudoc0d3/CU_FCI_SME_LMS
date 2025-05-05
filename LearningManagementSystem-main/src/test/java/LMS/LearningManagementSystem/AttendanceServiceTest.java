package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.Attendance;
import LMS.LearningManagementSystem.model.Lesson;
import LMS.LearningManagementSystem.repository.AttendanceRepository;
import LMS.LearningManagementSystem.repository.LessonRepository;
import LMS.LearningManagementSystem.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        attendanceService = new AttendanceService();
        attendanceService.lessonRepository = lessonRepository;
        attendanceService.attendanceRepository = attendanceRepository;
    }

    @Test
    void testMarkAttendanceWithValidOtp() {
        Long lessonId = 1L;
        String otp = "123456";
        int studentId = 101;

        Lesson lesson = new Lesson();
        lesson.setOtp(otp);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        boolean result = attendanceService.markAttendance(lessonId, otp, studentId);

        assertTrue(result);
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testMarkAttendanceWithInvalidOtp() {
        Long lessonId = 1L;
        String otp = "123456";
        int studentId = 101;

        Lesson lesson = new Lesson();
        lesson.setOtp("654321"); // Different OTP
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        boolean result = attendanceService.markAttendance(lessonId, otp, studentId);

        assertFalse(result);
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    void testMarkAttendanceLessonNotFound() {
        Long lessonId = 1L;
        String otp = "123456";
        int studentId = 101;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> attendanceService.markAttendance(lessonId, otp, studentId));
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
}
