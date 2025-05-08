package LMS.LearningManagementSystem;

import LMS.LearningManagementSystem.model.Notification;
import LMS.LearningManagementSystem.model.Role;
import LMS.LearningManagementSystem.model.Student;
import LMS.LearningManagementSystem.repository.NotificationRepository;
import LMS.LearningManagementSystem.repository.StudentRepository;
import LMS.LearningManagementSystem.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getUnReadNotifications()
    @Test
    void testGetUnReadNotifications() {
        // Arrange
        int userId = 1;
        Role userType = Role.STUDENT;
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserType(userType);
        notification.setIsRead(false);

        when(notificationRepository.findByUserIdAndUserTypeAndIsReadFalse(userId, userType)).thenReturn(List.of(notification));

        List<Notification> unreadNotifications = notificationService.getUnReadNotifications(userId, userType);

        // Assert
        assertNotNull(unreadNotifications);
        assertEquals(1, unreadNotifications.size());
        assertFalse(unreadNotifications.get(0).getIsRead());
    }

    // Test for getAllNotifications()
    @Test
    void testGetAllNotifications() {
        // Arrange
        int userId = 1;
        Role userType = Role.STUDENT;
        Notification notification1 = new Notification();
        notification1.setUserId(userId);
        notification1.setUserType(userType);

        Notification notification2 = new Notification();
        notification2.setUserId(userId);
        notification2.setUserType(userType);

        when(notificationRepository.findByUserIdAndUserType(userId, userType)).thenReturn(List.of(notification1, notification2));

        List<Notification> allNotifications = notificationService.getAllNotifications(userId, userType);

        // Assert
        assertNotNull(allNotifications);
        assertEquals(2, allNotifications.size());
    }

    // Test for getEmailForUser()
    @Test
    void testGetEmailForUser() {
        // Arrange
        int userId = 1;
        Student student = new Student();
        student.setEmail("test@student.com");
        when(studentRepository.findById(userId)).thenReturn(Optional.of(student));

        String email = notificationService.getEmailForUser(userId);

        // Assert
        assertEquals("test@student.com", email);
    }

    @Test
    void testGetEmailForUser_NotFound() {
        // Arrange
        int userId = 1;
        when(studentRepository.findById(userId)).thenReturn(Optional.empty());

        String email = notificationService.getEmailForUser(userId);

        // Assert
        assertNull(email);
    }

    // Test for sendEmailNotification()
    @Test
    void testSendEmailNotification() {
        // Arrange
        int userId = 1;
        String message = "This is a test notification";
        String email = "areem911@gmail.com";

        Student student = new Student();
        student.setEmail(email);
        when(studentRepository.findById(userId)).thenReturn(Optional.of(student));

        String result = notificationService.sendEmailNotification(userId, message);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        assertEquals("Notification sent successfully to your email.", result);
    }

    @Test
    void testSendEmailNotification_NoEmail() {
        // Arrange
        int userId = 1;
        String message = "This is a test notification";

        when(studentRepository.findById(userId)).thenReturn(Optional.empty());

        String result = notificationService.sendEmailNotification(userId, message);

        // Assert
        assertEquals("The email is not correct, please try again.", result);
    }

    // Test for createNotification()
    @Test
    void testCreateNotification() {
        // Arrange
        int userId = 1;
        Role userType = Role.STUDENT;
        String message = "Your grade is: 95";

        // Create a student
        Student student = new Student();
        student.setEmail("areem9114@gmail.com.com");
        student.setRole(userType);
        student.setId(userId);

        // Mock studentRepository to return the mock student
        when(studentRepository.findById(userId)).thenReturn(Optional.of(student));

        notificationService.createNotification(userId, userType, message);

        // Assert
        // Verify that notificationRepository.save() was called once
        verify(notificationRepository, times(1)).save(any(Notification.class));

        // Verify that mailSender.send() was called once
        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(mailCaptor.capture());

        // Verify that the email was sent with the correct message and recipient
        SimpleMailMessage sentMail = mailCaptor.getValue();
        assertEquals("areem9114@gmail.com.com", sentMail.getTo()[0]);
        assertEquals("Notification", sentMail.getSubject());
        assertEquals(message, sentMail.getText());
    }

    // Test for markAsRead()
    @Test
    void testMarkAsRead() {
        // Arrange
        Long notificationId = 1L;
        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setIsRead(false);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(notificationId);

        // Assert
        assertTrue(notification.getIsRead());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testMarkAsRead_NotificationNotFound() {
        // Arrange
        Long notificationId = 1L;
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        //  Assert & calling the function
        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(notificationId));
    }
}
