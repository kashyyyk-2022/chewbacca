package com.kashyyyk.chewbacca.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsrmResult {
    public String code;

    public Route[] routes;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        public double distance;

        public double duration;

        public RouteLeg[] legs;

        public String weight_name;

        public double weight;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RouteLeg {
        public double distance;

        public double duration;

        public RouteStep[] steps;

        public String summary;

        public double weight;   
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RouteStep {
        public double distance;

        public double duration;

        public String geometry;

        public String name;

        public String mode;

        public Meneuver maneuver;

        public Intersection[] intersections;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meneuver {
        public double bearing_after;

        public double bearing_before;

        public double[] location;

        public String modifier;

        public String type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Intersection {
        public double[] location;
    }
}
