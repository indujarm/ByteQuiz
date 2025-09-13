package com.project.quizApp.service;

import com.project.quizApp.DAO.QuestionDao;
import com.project.quizApp.model.Question;
import com.project.quizApp.model.QuestionWrapper;
import com.project.quizApp.model.Quiz;
import com.project.quizApp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.project.quizApp.DAO.QuizDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<Integer> createQuiz(String category, Integer numQ, String title) {
        try {
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
            if (questions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);
            quiz = quizDao.save(quiz);
            return new ResponseEntity<>(quiz.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id){
        Optional<Quiz> quiz = quizDao.findById(id);
        if (!quiz.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Question> questionFromDb = quiz.get().getQuestions();
        List<QuestionWrapper> questionFromUser = new ArrayList<>();
        for(Question  q : questionFromDb){
            QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            questionFromUser.add(questionWrapper);
        }
        return new ResponseEntity<>(questionFromUser, HttpStatus.OK);

    }

    public ResponseEntity<Integer> submitQuiz(Integer id, List<Response> responses) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Quiz quiz = quizOptional.get();
        List<Question> questions = quiz.getQuestions();
        int score = 0;

        for (Response response : responses) {
            for (Question question : questions) {
                if (question.getId().equals(response.getId())) {
                    if (response.getResponse().equalsIgnoreCase(question.getRightAnswer())) {
                        score++;
                    }
                    break; // Move to next response once matched
                }
            }
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }

}
