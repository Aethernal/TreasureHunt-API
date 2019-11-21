package core.bean;

import java.util.List;



public class Route {

    public enum RouteType {
        GET, OPTIONS, POST
    }

    /**
     * HTTP Action triggering this route
     */
    private RouteType type;

    /**
     * list of path corresponding to this service
     */
    private List<String> paths;
    /**
     * path to a service
     * should extends JsonService
     * core.services.json.TestService
     */
    private String service;


    public Route() {
    }

    public Route(List<String> paths, String service) {
        this.paths = paths;
        this.service = service;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }
}
