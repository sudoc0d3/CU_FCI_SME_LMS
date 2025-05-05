package LMS.LearningManagementSystem.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Getter
    private String otp; // Field to store the OTP for attendance

    @ManyToOne // Establishing many-to-one relationship
    @JoinColumn(name = "course_id") // Foreign key in the lessons table
    @Getter
    private Course course;

    public Lesson( String title, String otp, Course course) {
        this.title = title;
        this.otp = otp;
        this.course = course;
    }

    public Lesson() {

    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


}


