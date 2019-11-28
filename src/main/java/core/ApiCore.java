package core;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import core.bean.Route;
import core.bean.RouteContainer;
import core.services.json.JsonService;
import org.slf4j.LoggerFactory;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.io.*;
import java.util.*;

public enum ApiCore {
    INSTANCE;

    public static final Logger logger = (Logger) LoggerFactory.getLogger(ApiCore.class);
    public static final String configurationPath = "config/config.properties";

    // env variable
    public static final String mongoHostKey = "mongoHost";
    public static final String mongoPortKey = "mongoPort";
    public static final String mongoDBKey = "mongoDB";
    public static final String mongoUserKey = "mongoUser";
    public static final String mongoPassKey = "mongoPass";

    // config
    public static final String routePathKey = "routePath";

    private Properties configuration;
    private MongoClient mongo;
    private Gson gson = new Gson();

    /**
     * Create ApiCore INSTANCE
     * Initialize configuration & json tool
     */
    ApiCore() {

        try {
            var fileInput = new FileInputStream(configurationPath);
            configuration = new Properties();
            configuration.load(fileInput);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        logger.info("Starting HTTP Server");

        // use random free port
        var port = 0;

        // if port is passed as argument use it
        if (args.length > 0)
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.error("Invalid port value {}, Aborting !", args[0]);
                return;
            }

        try {
            var server = ApiCore.INSTANCE.startHTTPServer(port);

            logger.info("Started HTTP Server on port [{}] !", server.port());
            logger.info("Connecting to MongoDB !");

            // MongoDB Configuration from Properties
            String mongoHost = System.getenv(mongoHostKey);
            int mongoPort = Integer.parseInt(System.getenv(mongoPortKey));
            String mongoDB = System.getenv(mongoDBKey);
            String mongoUser = System.getenv(mongoUserKey);
            char[] mongoPass = System.getenv(mongoPassKey).toCharArray();

            MongoCredential credential = MongoCredential.createCredential(mongoUser, mongoDB, mongoPass);
            ApiCore.INSTANCE.mongo = MongoClients.create(
                    MongoClientSettings.builder()
                            .credential(credential)
                            .applyToClusterSettings
                                    (builder -> builder
                                            .hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort)))
                                            .maxWaitQueueSize(10000)
                                    )
                            .build()
            );

            // join main thread on http server
            server.onDispose().block();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    /**
     * @param file
     * @return content of file as String
     */
    public static String readFile(InputStream file) {
        var content = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return content.toString();
    }

    /**
     * @param port the HttpServer will listen on
     * @return DisposableServer Instance that listen on specified port
     * with loaded services routes
     * @throws Exception
     */
    public DisposableServer startHTTPServer(int port) throws Exception {

        // multiple exception thrown to cancel startup
        Map<String, RouteContainer> loadedRoutes = loadRoutes();

        return HttpServer.create()
                .port(port)
                .route(routes -> {
                            for (String path : loadedRoutes.keySet()) {
                                switch (loadedRoutes.get(path).getType()) {
                                    case POST:
                                        routes.post(path, loadedRoutes.get(path).getService());
                                        break;
                                    default:
                                        routes.get(path, loadedRoutes.get(path).getService());
                                        break;
                                }
                            }
                        }
                )
                .bindNow();
    }

    /**
     * @return Map containing route path, and route redirection object
     * @throws Exception
     */
    public Map<String, RouteContainer> loadRoutes() throws Exception {

        var routes = new HashMap<String, RouteContainer>();

        // FileNotFoundException if file is not found
        var content = readFile(new FileInputStream(configuration.getProperty(routePathKey)));

        // JsonSyntaxException if file is malformed
        Route[] routeArray = gson.fromJson(content, Route[].class);

        for (Route route : routeArray) {
            for (String path : route.getPaths()) {
                String className = route.getService();

                // log route in error but keep the others safe
                try {
                    var instance = Class.forName(className).getConstructor().newInstance();
                    routes.put(path, new RouteContainer((JsonService) instance, route.getType()));

                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }
        }

        return routes;
    }

    /**
     * @return mongo instance for request
     */
    public MongoClient getMongo() {
        return mongo;
    }

    /**
     * @return json tools instance
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * @return api configuration properties
     */
    public Properties getConfiguration() {
        return configuration;
    }
}
