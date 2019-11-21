package gpe.bean;

public class Position2D {

    double longitude;
    double latitude;

    public Position2D(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Position2D() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
