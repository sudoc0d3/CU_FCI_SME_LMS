package LMS.LearningManagementSystem;


import LMS.LearningManagementSystem.model.Lesson;
import LMS.LearningManagementSystem.repository.LessonRepository;
import LMS.LearningManagementSystem.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lesson = new Lesson();
        lesson.setId(1L);

    }

    @Test
    void testSetOtpForLesson() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));

        // Call the method to set the OTP for the lesson
        lessonService.setOtpForLesson(1L);
        verify(lessonRepository, times(1)).save(lesson);
        assert lesson.getOtp() != null && !lesson.getOtp().isEmpty();
    }

    @Test
    void testSetOtpForLesson_LessonNotFound() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert: Check that an exception is thrown when the lesson is not found
        try {
            lessonService.setOtpForLesson(1L);
        } catch (Exception e) {
            assert e instanceof NoSuchElementException;
        }

        // Verify that save was not called
        verify(lessonRepository, times(0)).save(any());
    }
}
