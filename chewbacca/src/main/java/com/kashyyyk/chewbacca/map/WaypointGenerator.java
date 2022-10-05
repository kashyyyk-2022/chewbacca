package com.kashyyyk.chewbacca.map;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

// Will create a list of locations for the route. The list will be read by CurrentRoute.html to draw the route on the map
public class WaypointGenerator {

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

    public WaypointGenerator() throws IOException {
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
    }

    public Iterator<Waypoint> getIterator() {
        return points.iterator();
    }

}
