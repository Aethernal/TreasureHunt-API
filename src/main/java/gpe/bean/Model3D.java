package gpe.bean;

public class Model3D {

    private String resource;
    private Vector3D position;
    private Vector3D rotation;
    private Vector3D taille;

    public Model3D(String resource, Vector3D position, Vector3D rotation, Vector3D taille) {
        this.resource = resource;
        this.position = position;
        this.rotation = rotation;
        this.taille = taille;
    }

    public Model3D() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }

    public Vector3D getTaille() {
        return taille;
    }

    public void setTaille(Vector3D taille) {
        this.taille = taille;
    }
}
