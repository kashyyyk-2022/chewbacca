package com.kashyyyk.chewbacca.map.lantmateriet;

import java.awt.image.BufferedImage;

import com.kashyyyk.chewbacca.map.Waypoint;

public class ImageProcessResult {
    public BufferedImage image;

    public int minNorth;

    public int minEast;

    public Waypoint[] waypoints;

    public ImageProcessResult(BufferedImage image, int minNorth, int minEast, Waypoint[] waypoints) {
        this.image = image;
        this.minNorth = minNorth;
        this.minEast = minEast;
        this.waypoints = waypoints;
    }
}
