package com.kashyyyk.chewbacca.map;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

// Will create a list of locations for the route. The list will be read by CurrentRoute.html to draw the route on the map
public class WaypointGenerator {
    private double totalDistance;
    private double totalElevation;
    //Create a preference type variable (ENUM)????? .

    public class Waypoint {
        private double lat;
        private double lng;

        public Waypoint(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() { return lat;}
        public double getLng() {return lng;}
    }

    private LinkedList<Waypoint> points;

    /**
     * This constructor call should generate a waypoint that fulfills all our preferences and criterias.
     *
     * Needs to be done:
     *  - checkDistance
     *  - checkElevation
     *  - checkTerrain
     *
     * @throws IOException
     */
    public WaypointGenerator() throws IOException {
        //TODO: Make the downloadData "box" adjust to the distance we want the route to be(?)
        Osm osm = OpenStreetMap.downloadData(57.692622286683225,11.966922283172607, 57.69652702997704,11.972200870513916);
        for (int i = 0; i < osm.node.length; i++) {
            System.out.println("Lat: " + osm.node[i].lat +" Lng: " + osm.node[i].lon);
            if(osm.node[i].tag != null) {
                for(int j = 0; j < osm.node[i].tag.length; j++) {
            //        System.out.println(osm.node[i].tag[j].k + " = " + osm.node[i].tag[j].v);
                }

            }
            if(osm.way[i].tag != null) {
                for(int j = 0; j < osm.way[i].tag.length; j++) {
                    System.out.println(osm.way[i].tag[j].k + " = " + osm.way[i].tag[j].v);
                }
            }

        }

        double endLat = osm.node[0].lat;
        double endLng = osm.node[0].lon;
        if(checkDistance(endLat, endLng) && checkElevation() && checkTerrain()){
            points.add(new Waypoint(endLat, endLng));
        }
    }

    public void testElevation() throws IOException{
        checkElevation();
    }

    public LinkedList<Waypoint> getRoute(){
        return points;
    }

    private void addWaypoint(double lat, double lng){

    }

    //TODO: Create a system that checks if the waypoint is out of our distance requirement.
    private boolean checkDistance(double Lat, double Lng){
        
        return false;
    }

    //TODO: Create a system that checks if the waypoint is out of our preferred elevation.
    private boolean checkElevation(){
        return true;
    }

    //TODO: Create a system that checks if the waypoint is considered a preferred terrain.
    private boolean checkTerrain(){
        return true;
    }

    public Iterator<Waypoint> getIterator() {
        return points.iterator();
    }

}
