package core.services.json;

import ch.qos.logback.classic.Logger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.ApiCore;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class JsonService implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JsonService.class);

    protected int state = 200; // may change if authentication is needed by a service
    protected Mono data; // service response
    protected Map<String, List<String>> queries; // multivalued map <- ?param1=X&param2=Y&param1=Z

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {

        // cors passthroughs
        if (request.method() == HttpMethod.OPTIONS) {
            return response
                    .addHeader("Access-Control-Allow-Origin", "*")
                    .addHeader("Access-Control-Allow-Methods", "*")
                    .addHeader("Access-Control-Allow-Headers", "*")
                    .status(200); // api state != service state
        }

        queries = new QueryStringDecoder(request.uri()).parameters();

        return response.addHeader("content-type", "application/json")
                .status(200) // api state != service state
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "*")
                .addHeader("Access-Control-Allow-Headers", "*")
                .sendString(handle(request, response).flatMap(jsonResponse -> jsonResponse.getResponse()))
                ;

    }

    protected abstract Mono<JsonResponse> handle(HttpServerRequest request, HttpServerResponse response);

    protected int getIntQueryParameter(String key, int def) {

        var opt = Optional.ofNullable(queries.get(key));
        if (opt.isPresent()) {
            try {
                return Integer.parseInt(opt.get().stream().findFirst().get());
            } catch (NumberFormatException e) {
                logger.error("query parameter [{}] has invalid value [{}]", key, opt.get());
                return def;
            }
        } else {
            return def;
        }
    }

    protected long getLongQueryParameter(String key, long def) {

        var opt = Optional.ofNullable(queries.get(key));
        if (opt.isPresent()) {
            try {
                return Long.parseLong(opt.get().stream().findFirst().get());
            } catch (NumberFormatException e) {
                logger.error("query parameter [{}] has invalid value [{}]", key, opt.get());
                return def;
            }
        } else {
            return def;
        }
    }

    protected boolean getBooleanQueryParameter(String key, boolean def) {

        var opt = Optional.ofNullable(queries.get(key));
        if (opt.isPresent()) {
            try {
                return Boolean.parseBoolean(opt.get().stream().findFirst().get());
            } catch (NumberFormatException e) {
                logger.error("query parameter [{}] has invalid value [{}]", key, opt.get());
                return def;
            }
        } else {
            return def;
        }
    }

    protected String getQueryParameter(String key, String def) {
        return queries.get(key).stream().findFirst().orElse(def);
    }

    protected JsonObject documentToJson(Document doc) {
        return new JsonParser().parse(doc.toJson()).getAsJsonObject();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Mono getData() {
        return data;
    }

    public void setData(Mono data) {
        this.data = data;
    }

    public Map<String, List<String>> getQueries() {
        return queries;
    }

}
