package LMS.LearningManagementSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "assignment_Logs")
@Getter
@Setter
public class AssignmentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private Integer studentId;

//    @JoinColumn(name = "assignment_Id")
//    private Integer assignmentId;

    private String answeredPdfPath;

    // Change from primitive Integer to Integer to allow null values
    private Integer grade;

    @ManyToOne
    @JoinColumn(name = "assignment_Id")
    private Assignment assignment;

    // Constructors
    public AssignmentLog() {}

    public AssignmentLog(Integer studentId, String answeredPdfPath, Integer grade) {
        this.studentId = studentId;
        this.answeredPdfPath = answeredPdfPath;
        this.grade = grade;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }


//    public Integer getAssignmentId() {
//        return assignmentId;
//    }
//
//    public void setAssignmentId(Integer assignmentId) {
//        this.assignmentId = assignmentId;
//    }

    public String getAnsweredPdfPath() {
        return answeredPdfPath;
    }

    public void setAnsweredPdfPath(String answeredPdfPath) {
        this.answeredPdfPath = answeredPdfPath;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}