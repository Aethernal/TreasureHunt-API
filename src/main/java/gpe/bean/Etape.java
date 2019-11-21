package gpe.bean;

import java.util.List;

public class Etape {

    private int order;
    private Position2D position;
    private List<Model3D> models;
    private Question question;

    public Etape(int order, Position2D position, List<Model3D> models, Question question) {
        this.order = order;
        this.position = position;
        this.models = models;
        this.question = question;
    }

    public Etape() {
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Position2D getPosition() {
        return position;
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }

    public List<Model3D> getModels() {
        return models;
    }

    public void setModels(List<Model3D> models) {
        this.models = models;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
