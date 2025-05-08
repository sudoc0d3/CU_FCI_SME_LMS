package LMS.LearningManagementSystem.repository;

import LMS.LearningManagementSystem.model.Notification;
import LMS.LearningManagementSystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndUserType(int userId, Role userType);
    List<Notification> findByUserIdAndUserTypeAndIsReadFalse(int userId, Role userType);
}
