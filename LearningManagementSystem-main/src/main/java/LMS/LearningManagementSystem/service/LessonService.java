package LMS.LearningManagementSystem.service;
import LMS.LearningManagementSystem.model.Lesson;
import org.springframework.stereotype.Service;
import LMS.LearningManagementSystem.repository.LessonRepository;
import java.security.SecureRandom;


@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    // Method to generate OTP
    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
        return String.valueOf(otp);
    }

    public void setOtpForLesson(Long lessonId) {
        // Fetch the lesson by ID
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        String otp = generateOtp();
        lesson.setOtp(otp);
        lessonRepository.save(lesson);
    }
}
