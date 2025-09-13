package com.project.quizApp.controller;

import com.project.quizApp.DAO.QuizDao;
import com.project.quizApp.model.QuestionWrapper;
import com.project.quizApp.model.Quiz;
import com.project.quizApp.model.Response;
import com.project.quizApp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    QuizDao quizDao;  // ✅ fix: inject QuizDao

    @PostMapping("create")
    public ResponseEntity<Integer> createQuiz(@RequestParam String category,
                                              @RequestParam Integer numQ,
                                              @RequestParam String title) {
        return quizService.createQuiz(category, numQ, title);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> responses) {
        return quizService.submitQuiz(id, responses);
    }

    @GetMapping("all")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        try {
            List<Quiz> quizzes = quizDao.findAll(); // ✅ fix: quizDao now injected
            return new ResponseEntity<>(quizzes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
