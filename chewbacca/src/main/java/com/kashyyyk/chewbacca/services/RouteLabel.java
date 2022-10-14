package com.kashyyyk.chewbacca.services;

/**
 * A label to be used in a route
 */
public class RouteLabel {
    
    /**
     * The position of the label
     */
    private double[] position;

    /**
     * The text of the label
     */
    private String text;

    /**
     * Create a new route label
     * 
     * @param position  the position of the label
     * @param text      the text of the label
     */
    public RouteLabel(double[] position, String text) {
        this.position = position;
        this.text = text;
    }

    /**
     * Get the position of the label
     * 
     * @return the position of the label
     */
    public double[] getPosition() {
        return position;
    }

    /**
     * Get the text of the label
     * 
     * @return the text of the label
     */
    public String getText() {
        return text;
    }

    /**
     * Set the position of the label
     * 
     * @param position  the position of the label
     */
    public void setPosition(double[] position) {
        this.position = position;
    }

    /**
     * Set the text of the label
     * 
     * @param text  the text of the label
     */
    public void setText(String text) {
        this.text = text;
    }
}
