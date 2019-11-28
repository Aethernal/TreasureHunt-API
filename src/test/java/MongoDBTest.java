import com.google.gson.Gson;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import core.ApiCore;
import gpe.bean.Parcourt;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MongoDBTest {

    private Properties configuration;
    private MongoClient mongo;
    private Gson gson;

    @Before
    public void setup() {
        gson = new Gson();
        try {
            var fileInput = new FileInputStream(ApiCore.configurationPath);
            configuration = new Properties();
            configuration.load(fileInput);
        } catch (IOException e) {
            System.exit(1);
        }
        String mongoHost = System.getenv(ApiCore.mongoHostKey);
        int mongoPort = Integer.parseInt(System.getenv(ApiCore.mongoPortKey));
        String mongoDB = System.getenv(ApiCore.mongoDBKey);
        String mongoUser = System.getenv(ApiCore.mongoUserKey);
        char[] mongoPass = System.getenv(ApiCore.mongoPassKey).toCharArray();

        MongoCredential credential = MongoCredential.createCredential(mongoUser, mongoDB, mongoPass);
        mongo = MongoClients.create(
                MongoClientSettings.builder()
                        .credential(credential)
                        .applyToClusterSettings
                                (builder -> builder
                                        .hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)))
                                        .maxWaitQueueSize(10000)
                                )
                        .build()
        );

    }

    @Test
    public void getParcours() {
        List<Parcourt> parcours = Parcourt.getParcourts(mongo).collectList().block();
        assert parcours.size() > 0;
    }

}
