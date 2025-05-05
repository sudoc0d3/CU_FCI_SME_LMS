/*package LMS.LearningManagementSystem.controller;

import LMS.LearningManagementSystem.model.Quiz;
import LMS.LearningManagementSystem.model.QuizLog;
import LMS.LearningManagementSystem.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/add")
    public Quiz addQuiz(@RequestBody Quiz quiz) {
        return quizService.addQuiz(quiz);
    }

    @DeleteMapping("/delete/{quizId}")
    public void deleteQuiz(@PathVariable Integer quizId) {
        quizService.deleteQuiz(quizId);
    }

    @PutMapping("/update-grade/{quizId}/{totalGrade}")
    public void updateQuizTotalGrade(@PathVariable Integer quizId, @PathVariable Integer totalGrade) {
        quizService.updateQuizTotalGrade(quizId, totalGrade);
    }

    @PostMapping("/answer")
    public void answerQuiz(@RequestParam String token, @RequestParam Integer quizId, @RequestBody List<String> rightAnswers) {
        quizService.answerQuiz(token, quizId, rightAnswers);
    }

    @GetMapping("/logs")
    public List<QuizLog> viewAllQuizLogs() {
        return quizService.getAllQuizLogs();
    }

    @GetMapping("/logs/{quizId}")
    public List<QuizLog> viewQuizLogsForSpecificQuiz(@PathVariable Integer quizId) {
        return quizService.getQuizLogsByQuizId(quizId);
    }

    @DeleteMapping("/logs/delete/{quizLogId}")
    public void deleteQuizLog(@PathVariable Integer quizLogId) {
        quizService.deleteQuizLog(quizLogId);
    }
}*/