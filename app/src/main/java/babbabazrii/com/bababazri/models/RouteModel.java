package babbabazrii.com.bababazri.models;

import java.io.Serializable;

public class RouteModel implements Serializable {

    private static final long serialVersionUID = 1L;
    String distance;
    String duration;
    String valueDistance;
    String selectedRoute;

    public String getSelectedRoute() {
        return selectedRoute;
    }

    public void setSelectedRoute(String selectedRoute) {
        this.selectedRoute = selectedRoute;
    }

    public String getValueDistance() {
        return valueDistance;
    }

    public void setValueDistance(String valueDistance) {
        this.valueDistance = valueDistance;
    }

    public String getValueDuration() {
        return valueDuration;
    }

    public void setValueDuration(String valueDuration) {
        this.valueDuration = valueDuration;
    }

    String valueDuration;


    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
