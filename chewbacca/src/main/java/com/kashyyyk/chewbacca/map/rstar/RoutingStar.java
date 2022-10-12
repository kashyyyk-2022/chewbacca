package com.kashyyyk.chewbacca.map.rstar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import com.kashyyyk.chewbacca.map.OpenSourceRoutingMachine;
import com.kashyyyk.chewbacca.map.Osm;
import com.kashyyyk.chewbacca.map.OsmDatabase;
import com.kashyyyk.chewbacca.map.Point;
import com.kashyyyk.chewbacca.map.Osm.Node;
import com.kashyyyk.chewbacca.map.Osm.Way;
import com.kashyyyk.chewbacca.services.RouteLabel;

/**
 * <h4>R* — Routing Star</h4>
 * 
 * <p>Routing Star is an algorithm for generating a route on a graph following a
 * set of constraints and biases. It is a variant of the A* algorithm.</p>
 * 
 * <p>In constrast to A*, R* does not seek an <b>end</b> node, but rather seeks to
 * find a path around a graph that satisfies a set of constraints and biases.</p>
 * 
 * <p>Prototype by Wincent.</p>
 */
public class RoutingStar {
    /**
     * The start point of the route.
     */
    public Point start;

    /**
     * The ideal distance of the route
     */
    public double idealDistance;

    /**
     * The ideal terrain of the route
     */
    public String[] idealTerrain;

    /**
     * The ideal surface of the route
     */
    public String[] idealSurface;

    /**
     * The bias for the distance
     */
    public float distanceBias;

    /**
     * The bias for the distance to the start
     */
    public float distanceToStartBias;

    /**
     * The bias for the elevation
     */
    public double elevationBias;

    /**
     * The bias for the terrain
     */
    public float terrainBias;

    /**
     * The bias for the surface
     */
    public float surfaceBias;

    /**
     * RNG seed
     */
    public long seed;

    /**
     * RNG
     */
    private Random random;

    /**
     * Random bias
     */
    public double randomBias;

    /**
     * OsmDatabase
     */
    private OsmDatabase database;

    /**
     * The current route
     */
    private ArrayList<Point> route;

    /**
     * The current route in nodes
     */
    private ArrayList<Long> routeNodes;

    /**
     * The current distance
     */
    private float distance;

    /**
     * The linked routing star graph
     */
    private RoutingStarGraph graph;

    /**
     * The priority queue
     */
    private PriorityQueue<REntry> priorityQueue;

    /**
     * Visited nodes
     */
    private HashSet<Long> visited;

    /**
     * Avoid nodes
     */
    private HashSet<Long> avoid;

    /**
     * The start of the route
     */
    private Point startPoint;

    /**
     * The end of the route
     */
    private Point endPoint;

    /**
     * Labels for the nodes
     */
    private Map<Point, String> labels;

    /**
     * Get the route
     * 
     * @return the route
     */
    public List<Point> getRoute() {
        return route;
        /*var result = new ArrayList<Point>();

        var previous = route.get(0);
        // Use OSRM to get the route between the points
        for (var i = 1; i < route.size(); i++) {
            var current = route.get(i);

            result.addAll(runAStar(previous, current));

            previous = current;
        }

        return result;*/
    }

    /**
     * Get the start point
     * 
     * @return the start point
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * Get the end point
     * 
     * @return the end point
     */
    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * Get the labels
     * 
     * @return the labels
     */
    public RouteLabel[] getLabels() {
        var result = new RouteLabel[labels.size()];

        var i = 0;

        for (var entry : labels.entrySet()) {
            
            result[i] = new RouteLabel(new double[] {
                entry.getKey().getLatitude(),
                entry.getKey().getLongitude()
            }, entry.getValue());

            i++;
        }

        return result;
    }

    /**
     * Get the database
     * 
     * @return the database
     */
    public OsmDatabase getDatabase() {
        return database;
    }

    public void Initialize() throws IOException
    {
        random = new Random(seed);

        graph = new RoutingStarGraph();

        database = new OsmDatabase(graph);

        route = new ArrayList<Point>();

        routeNodes = new ArrayList<Long>();

        visited = new HashSet<Long>();

        avoid = new HashSet<Long>();

        labels = new HashMap<Point, String>();

        distance = 0;

        database.downloadData(start);

        var origin = graph.findNearestNode(start);

        startPoint = origin.point;

        var startNode = runRStar(null).node;

        endPoint = startNode.point;

        start = startNode.point;

        distanceToStartBias = 1;
        terrainBias = 1;
        
        visited.clear();

        // Add the ID of each node from the graph to the visited list
        for (var node : routeNodes) {
            avoid.add(node);
        }
        
        runRStar(origin);

        //runAStar(start, origin.point);

        /*
        var paths = database.queryWays("highway", "track", "path");

        for (var path : paths) {
            var nodes = database.getNodes(path);

            for (var node : nodes) {
                route.add(new Point(node.lat, node.lon));
            }
        } */
    }

    private REntry runRStar(RNode destination) throws IOException {
        var features = graph.getFeatures("water");  //todo Ensure that features is collected from graph
        var nearest = graph.findNearestNode(start);

        
        //visited.clear();

        priorityQueue = new PriorityQueue<REntry>((a, b) -> {
            return Comparator.comparingDouble((REntry r) -> r.priority).compare(a, b);
        });

        priorityQueue.add(new REntry(
            nearest,
            0,
            null,
            0,
            null
        ));

        int iterations = 0;

        REntry end = null;

        int maxDownloads = 5;

        while (!priorityQueue.isEmpty())
        {
            // Calculate the priorities for the queue entries
            priorityQueue.forEach((REntry entry) -> {
                /*if (destination != null) {
                    entry.priority = Point.comparableDistance(entry.node.point, destination.point);

                    return;
                }*/

                if (entry.previous == null) {
                    labels.put(entry.node.point, "Start");

                    return;
                }

                var label = new StringBuilder();

                var cost = entry.cost;
                label.append(String.format("Cost: %.5f\r\n", cost));
                var distancePart = 0.0; //distanceToStartBias * Point.comparableDistance(entry.node.point, start);
                label.append(String.format("Distance: %.5f\r\n", distancePart));
                var terrainPart = terrainBias * getClosestFeaturePoint(entry.node.point, features);
                label.append(String.format("Terrain: %.5f\r\n", terrainPart));
                var elevationPart = elevationBias * Math.abs(entry.node.elevation - entry.previous.node.elevation);
                label.append(String.format("Elevation: %.5f\r\n", elevationPart));
                entry.priority = cost + (distancePart + terrainPart + elevationPart) * Point.distance(entry.node.point, entry.previous.node.point);
                // If we are avoiding this node, then increase the priority
                if (avoid.contains(entry.node.id)) {
                    entry.priority *= 100;
                }
                if (destination != null) {
                    entry.priority *= Point.distance(entry.node.point, destination.point) * 0.01;
                }
                entry.priority += random.nextDouble() * randomBias;
                label.append(String.format("Priority: %.5f\r\n", entry.priority));
                labels.put(entry.node.point, label.toString());

                return;
            });

            iterations++;

            final var currentEntry = priorityQueue.poll();
            final var current = currentEntry.node;

            if (visited.contains(current.id)) {
                continue;
            }

            visited.add(current.id);

            end = currentEntry;

            if (destination != null && current == destination) {
                break;
            }

            if (destination == null && (currentEntry.distance > idealDistance || Point.distance(start, current.point) > idealDistance / 2)) {
                break;
            }

            var connected = graph.getConnected(current);

            if (connected.size() == 1 && maxDownloads > 0) {
                database.downloadData(current.point);

                connected = graph.getConnected(current);

                maxDownloads--;

                System.out.println("Downloaded data");
            }

            for (RNode edgeNode : connected) {
                final var cost = currentEntry.priority;

                final var edgeEntry = new REntry(
                    edgeNode,
                    cost,
                    current,
                    currentEntry.cost,
                    currentEntry
                );

                edgeEntry.distance = currentEntry.distance + Point.distance(current.point, edgeNode.point);

                priorityQueue.add(edgeEntry);
            }
        }

        System.out.println("Iterations: " + iterations);

        if (end != null) {
            var current = end;

            while (current != null) {
                route.add(current.node.point);
                routeNodes.add(current.node.id);
                current = current.previous;
            }

            //Collections.reverse(route);
        }

        return end;
    }

    /**
     * @param point - point to check distance to feature
     * @param features - set of all features that fulfill user demands
     * @return distance to feature if the point is outside the feature area. 0 if point is within feature area.
     */
    private static double getClosestFeaturePoint(Point point, Set<RFeature> features) {
        var closest = Double.MAX_VALUE;
            for (var feature : features) {
            var closestPoint = feature.closestPoint(point);

            if (closestPoint == null) continue;
            var distance = Point.distance(point, closestPoint); //!feature.contains(point) ? Point.comparableDistance(point, closestPoint) : 0;
            if (distance < closest) {
                closest = distance;
            }
        }

        return closest;
    }

}
