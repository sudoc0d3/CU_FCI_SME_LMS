package LMS.LearningManagementSystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title; // Question title or body

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;
    private String correctAnswer; // Correct answer for automated grading


    @ElementCollection
    private List<String> options;

//    public Question() {
//    }
//
//    public Question(String title, Course course, String correctAnswer, Integer grade) {
//        this.title = title;
//        this.course = course;
//        this.correctAnswer = correctAnswer;
//        this.grade = grade;
//        this.options = null;
//    }
//
//    public Question(String title, Course course, String correctAnswer, Integer grade, List<String> options) {
//        this.title = title;
//        this.course = course;
//        this.correctAnswer = correctAnswer;
//        this.grade = grade;
//        this.options = options;
//    }


    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }



    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGrade(int i) {
    }
}
