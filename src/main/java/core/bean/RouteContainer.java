package core.bean;


import core.services.json.JsonService;

public class RouteContainer {

    private JsonService service;
    private Route.RouteType type;

    public RouteContainer() {
    }

    public RouteContainer(JsonService service, Route.RouteType type) {
        this.service = service;
        this.type = type;
    }

    public JsonService getService() {
        return service;
    }

    public void setService(JsonService service) {
        this.service = service;
    }

    public Route.RouteType getType() {
        return type;
    }

    public void setType(Route.RouteType type) {
        this.type = type;
    }
}
