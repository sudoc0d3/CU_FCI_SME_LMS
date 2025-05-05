package LMS.LearningManagementSystem.service;
import LMS.LearningManagementSystem.model.Notification;
import LMS.LearningManagementSystem.model.Role;
import LMS.LearningManagementSystem.model.Student;
import LMS.LearningManagementSystem.repository.NotificationRepository;
import LMS.LearningManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    private StudentRepository studentRepository;
    private JavaMailSender mailSender;

    public NotificationService(StudentRepository studentRepository, JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.studentRepository = studentRepository;
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    public List<Notification> getUnReadNotifications(int userId , Role userType) {
        return notificationRepository.findByUserIdAndUserTypeAndIsReadFalse(userId,userType);
    }

    public List<Notification> getAllNotifications(int userId, Role userType) {
        return notificationRepository.findByUserIdAndUserType(userId,userType);
    }
    public String getEmailForUser(int userId) {
        Optional<Student> studentOptional = studentRepository.findById(userId);
        return studentOptional.map(Student::getEmail).orElse(null);
    }
    public String sendEmailNotification(int userId, String message) {
        String email = getEmailForUser(userId);

        if (email != null) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("Notification");
                mailMessage.setText(message);
                mailSender.send(mailMessage);
                return "Notification sent successfully to your email.";
            } catch (Exception e) {
                e.printStackTrace();  // Log the exception stack trace for debugging
                return "Error sending email notification: " + e.getMessage();
            }
        }
        return "The email is not correct, please try again.";
    }

    public void createNotification(int userId, Role userType, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserType(userType);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        // Send Email Notification
        sendEmailNotification(userId, message);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));;
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
