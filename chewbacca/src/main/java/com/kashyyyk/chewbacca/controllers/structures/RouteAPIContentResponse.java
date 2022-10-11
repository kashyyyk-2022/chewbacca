package com.kashyyyk.chewbacca.controllers.structures;

/**
 * A response for the route api content
 */
public class RouteAPIContentResponse {

    /**
     * The id of the route
     */
    private String id;

    /**
     * If the route is done
     */
    private boolean done;

    /**
     * The route
     */
    private double[][] route;

    /**
     * The start of the route
     */
    private double[] start;

    /**
     * The end of the route
     */
    private double[] end;

    /**
     * Create a new route content response
     * 
     * @param id    the id of the route
     * @param done  if the route is done
     * @param route the route
     */
    public RouteAPIContentResponse(String id, boolean done, double[][] route, double[] start, double[] end) {
        this.id = id;
        this.done = done;
        this.route = route;
        this.start = start;
        this.end = end;
    }

    /**
     * Get the id of the route
     * 
     * @return the id of the route
     */
    public String getId() {
        return id;
    }

    /**
     * Get if the route is done
     * 
     * @return true if the route is done, false otherwise
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Get the route
     * 
     * @return the route
     */
    public double[][] getRoute() {
        return route;
    }

    /**
     * Set the id of the route
     * 
     * @param id the new id of the route
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set if the route is done
     * 
     * @param done the new done value
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Set the route
     * 
     * @param route the new route
     */
    public void setRoute(double[][] route) {
        this.route = route;
    }

    /**
     * Get the start of the route
     * 
     * @return the start of the route
     */
    public double[] getStart() {
        return start;
    }

    /**
     * Set the start of the route
     * 
     * @param start the new start of the route
     */
    public void setStart(double[] start) {
        this.start = start;
    }
}
