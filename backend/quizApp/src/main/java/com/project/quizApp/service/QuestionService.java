package com.project.quizApp.service;

import com.project.quizApp.DAO.QuestionDao;
import com.project.quizApp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;
//get all questions
    public ResponseEntity<List<Question>> getAllQuestions() {
        try{
        return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }
//get questions by category
    public ResponseEntity<List<Question>>getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }
//add question
    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Question not added",HttpStatus.BAD_REQUEST);
    }
//update question
    public ResponseEntity<String> updateQuestion(Integer id, Question updatedQuestion) {
        try {
            Optional<Question> optionalQuestion = questionDao.findById(id);
            if (optionalQuestion.isPresent()) {
                Question existing = optionalQuestion.get();
                existing.setQuestionTitle(updatedQuestion.getQuestionTitle());
                existing.setOption1(updatedQuestion.getOption1());
                existing.setOption2(updatedQuestion.getOption2());
                existing.setOption3(updatedQuestion.getOption3());
                existing.setOption4(updatedQuestion.getOption4());
                existing.setRightAnswer(updatedQuestion.getRightAnswer());
                existing.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
                existing.setCategory(updatedQuestion.getCategory());
                questionDao.save(existing);
                return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Question not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating question: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//delete question
    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            if (questionDao.existsById(id)) {
                questionDao.deleteById(id);
                return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Question not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting question: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
