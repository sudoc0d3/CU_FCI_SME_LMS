package LMS.LearningManagementSystem.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "courses")
public class Course {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseID")
    protected int id;
    @Setter
    @Getter
    @Column(nullable = false) // Ensure course name cannot be null
    protected String courseTitle;

    @Setter
    @Getter
    @Column(nullable = true) // Ensure course name cannot be null
    protected String courseDescription;

    @Setter
    @Getter
    @Column(nullable = true) // Ensure course name cannot be null
    protected int courseDuration;
    

    @Getter
    @Setter
    @ManyToOne // Each course is associated with one instructor
    @JoinColumn(name = "instructor_id") // Foreign key in the courses table
    private Instructor instructor;

    @Getter
    @ElementCollection
    @CollectionTable(name = "course_media", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "media_url")
    private List<String> mediaFiles = new ArrayList<>();


    @Setter
    @Getter
    @ManyToMany(mappedBy = "courses")
    private List<Student> enrolledStudents = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "course") // Establishing one-to-many relationship
    private List<Lesson> lessons = new ArrayList<>();



    public Course(String courseTitle, String courseDescription, int courseDuration,Instructor instructor) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseDuration = courseDuration;
        this.instructor=instructor;
    }

    public Course() {

    }

    public Course(String courseTitle, int courseDuration) {
        this.courseTitle = courseTitle;
        this.courseDuration = courseDuration;
    }

    public void addStudent(Student student){
        enrolledStudents.add(student);
    }

    public void setMediaFiles(ArrayList<Object> objects) {
    }

    public void setId(Integer courseId) {
    }
}