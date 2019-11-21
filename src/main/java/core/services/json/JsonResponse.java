package core.services.json;

import com.google.gson.Gson;
import core.ApiCore;
import reactor.core.publisher.Mono;

public class JsonResponse {

    /**
     * json response template
     * {
     *   "state": 200,
     *   "data": {
     *     "name":"test"
     *   }
     * }
     */
    private static final String base = "{\n\"state\": %s,\n\"data\": %s\n}";

    private int state;
    private Mono data;

    public JsonResponse(int state) {
        this.state = state;
    }

    public JsonResponse(int state, Mono data) {
        this.state = state;
        this.data = data;
    }

    public Mono<String> getResponse() {
        var gson = ApiCore.INSTANCE.getGson();
        return getData().map(o -> String.format(base, getState(), gson.toJson(o)));
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Mono getData() {
        return data != null ? data : Mono.empty();
    }

    public void setData(Mono data) {
        this.data = data;
    }
}
