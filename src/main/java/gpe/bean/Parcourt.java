package gpe.bean;

import com.mongodb.reactivestreams.client.MongoClient;
import core.ApiCore;
import reactor.core.publisher.Flux;
import java.util.List;

public class Parcourt {

    public static final String mongoDatabaseKey = "mongoDatabase";
    public static final String mongoParcourtCollectionKey = "mongoParcourtCollection";

    private String identifiant;
    private Position2D position;
    private String nom;
    private String createur;
    private List<Etape> etapes;
    private int difficulte;

    public static Flux<Parcourt> getParcourts(MongoClient client) {

        var gson = ApiCore.INSTANCE.getGson();
        var configuration = ApiCore.INSTANCE.getConfiguration();

        var database = System.getenv(mongoDatabaseKey);
        var collection = configuration.getProperty( mongoParcourtCollectionKey, "parcourt");

        var collectionInstance = client.getDatabase(database).getCollection(collection);

        return Flux.from(collectionInstance.find())
                .map(document -> {
                        var parcourt = gson.fromJson(document.toJson(), Parcourt.class);
                        parcourt.setIdentifiant(document.getObjectId("_id").toHexString());
                        return parcourt;
                    }
                );
    }

    public Parcourt(Position2D position, String nom, String createur, List<Etape> etapes, int difficulte) {
        this.position = position;
        this.nom = nom;
        this.createur = createur;
        this.etapes = etapes;
        this.difficulte = difficulte;
    }

    public Parcourt() {
    }

    public Position2D getPosition() {
        return position;
    }

    public void setPosition(Position2D position) {
        this.position = position;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public List<Etape> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<Etape> etapes) {
        this.etapes = etapes;
    }

    public int getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(int difficulte) {
        this.difficulte = difficulte;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
}
