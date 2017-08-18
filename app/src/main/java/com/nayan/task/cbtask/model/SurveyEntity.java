package com.nayan.task.cbtask.model;

/**
 * Created by nayan on 18/8/17.
 */

public class SurveyEntity {

    private String id = "";
    private String date = "";
    private String question = "";
    private String answer = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
