package gpe.bean;


import java.util.List;

public class Question {

    private String question;
    private String reponse;

    public Question(String question, String reponse) {
        this.question = question;
        this.reponse = reponse;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
