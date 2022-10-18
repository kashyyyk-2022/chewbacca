package com.kashyyyk.chewbacca.controllers.structures;

/***
 * This class represents the response form the server when the client asks whether it is done with the route generation or not for a given ID.
 */

public class RouteAPIDoneResponse {



    /***
     * A string representing the randomly generated ID for the generation of a route for a given user.
     */
    private String id;

    /***
     * A boolean value which should be true if the server is done generating the route and false if otherwise.
     */
    private boolean done;

    /***
     * Constructor for the class RouteAPIDoneResponse
     * @param id The string representing the randomly generated ID for the generation of a route for a given user.
     * @param done Should be true if the server is done generating the route and false if otherwise.
     */
    public RouteAPIDoneResponse(String id, boolean done){
        this.id = id;
        this.done = done;
    }

    /***
     * Allows the value of ID to be changed.
     * @param id The new value of ID.
     */
    public void setId(String id){
        this.id = id;
    }

    /***
     * Allows the value of done to be changed.
     * @param done The new value of done.
     */
    public void setDone(boolean done){
        this.done = done;
    }

    /***
     *
     * @return The current value of ID.
     */
    public String getId(){
        return this.id;
    }

    /***
     *
     * @return The current value of done.
     */
    public boolean getDone(){
        return this.done;
    }

}
