package com.kashyyyk.chewbacca.services;

/**
 * A route object
 */
public class LocalRoute {

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
     * Create a new route
     * 
     * @param id    the id of the route
     * @param done  if the route is done
     * @param route the route
     */
    public LocalRoute(String id, boolean done, double[][] route, double[] start, double[] end) {
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
     * Get the start of the route
     * 
     * @return the start of the route
     */
    public double[] getStart() {
        return start;
    }

    /**
     * Get the end of the route
     * 
     * @return the end of the route
     */
    public double[] getEnd() {
        return end;
    }

    /**
     * Set if the route is done
     * 
     * @param done true if the route is done, false otherwise
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Set the route
     * 
     * @param route the route
     */
    public void setRoute(double[][] route) {
        this.route = route;
    }

    /**
     * Set the start of the route
     * 
     * @param start the start of the route
     */
    public void setStart(double[] start) {
        this.start = start;
    }

    /**
     * Set the end of the route
     * 
     * @param end the end of the route
     */
    public void setEnd(double[] end) {
        this.end = end;
    }
}
