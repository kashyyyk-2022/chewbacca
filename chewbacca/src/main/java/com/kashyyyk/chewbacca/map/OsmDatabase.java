package com.kashyyyk.chewbacca.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.kashyyyk.chewbacca.map.Osm.*;

/**
 * A database of Open Street Map data
 */
public class OsmDatabase {

    /**
     * The size of the download area in kilometers
     */
    private final double DOWNLOAD_SIZE = 1;

    /**
     * HashMap of all nodes
     */
    private final HashMap<Long, Node> nodes = new HashMap<Long, Node>();

    /**
     * HashMap of all ways
     */
    private final HashMap<Long, Way> ways = new HashMap<Long, Way>();

    /**
     * HashMap of all relations
     */
    private final HashMap<Long, Relation> relations = new HashMap<Long, Relation>();

    /**
     * The current bounds of the database
     */
    private Bounds bounds;

    /**
     * The graph
     */
    private OsmGraph graph;

    /**
     * Create a new OSM database
     * 
     * @param graph                     Linked graph
     */
    public OsmDatabase(OsmGraph graph) {
        this.graph = graph;

        this.graph.setDatabase(this);
    }

    /**
     * Get the nodes HashMap
     * 
     * @return                          Nodes HashMap
     */
    public HashMap<Long, Node> getNodes() {
        return nodes;
    }

    /**
     * Get the ways HashMap
     * 
     * @return                          Ways HashMap
     */
    public HashMap<Long, Way> getWays() {
        return ways;
    }

    /**
     * Get the relations HashMap
     * 
     * @return                          Relations HashMap
     */
    public HashMap<Long, Relation> getRelations() {
        return relations;
    }

    /**
     * Download data from Open Street Map, and store it in the database
     * 
     * @param bottomLeftLat             Bottom left latitude
     * @param bottomLeftLon             Bottom left longitude
     * @param topRightLat               Top right latitude
     * @param topRightLon               Top right longitude
     */
    public void downloadData(double bottomLeftLat, double bottomLeftLon, double topRightLat, double topRightLon) throws IOException {
        Osm osm = OpenStreetMap.downloadData(bottomLeftLat, bottomLeftLon, topRightLat, topRightLon);

        // Expand bounds
        if (bounds == null) {
            bounds = osm.bounds;
        } else {
            bounds.minlat = Math.min(bounds.minlat, osm.bounds.minlat);
            bounds.minlon = Math.min(bounds.minlon, osm.bounds.minlon);
            bounds.maxlat = Math.max(bounds.maxlat, osm.bounds.maxlat);
            bounds.maxlon = Math.max(bounds.maxlon, osm.bounds.maxlon);
        }

        // Add nodes
        for (Node node : osm.node) {
            if (nodes.containsKey(node.id)) continue;

            nodes.put(node.id, node);

            graph.processNode(node);
        }

        var newNodes = new ArrayList<Long>();
        var newPoints = new ArrayList<Double>();

        // Add ways
        for (Way way : osm.way) {
            if (ways.containsKey(way.id)) continue;

            ways.put(way.id, way);

            graph.processWay(way);

            var nodes = getNodes(way);

            for (var node : nodes) {
                newNodes.add(node.id);
                newPoints.add(node.lat);
                newPoints.add(node.lon);
            }
            
            /*
            newNodes.add(nodes[0].id);
            newPoints.add(nodes[0].lat);
            newPoints.add(nodes[0].lon);

            newNodes.add(nodes[nodes.length - 1].id);
            newPoints.add(nodes[nodes.length - 1].lat);
            newPoints.add(nodes[nodes.length - 1].lon);
            */
        }

        var elevationGrid = new HashMap<Point, Double>();
        var elevationList = new ArrayList<Point>();
        var elevationPoints = new ArrayList<Double>();

        // Devide the new nodes into grids
        for (var i = 0; i < newNodes.size(); i++) {
            var lat = newPoints.get(i * 2);
            var lon = newPoints.get(i * 2 + 1);
            
            // Round to 3 decimal places
            var point = new Point((int) (lat * 1000), (int) (lon * 1000));

            if (!elevationGrid.containsKey(point)) {
                elevationGrid.put(point, 0.0);
                elevationPoints.add(lat);
                elevationPoints.add(lon);
                elevationList.add(point);
            }
        }

        System.out.println("Downloading elevations for " + elevationList.size() + " nodes");

        // Add elevations
        // Get the elvation in batches of 100
        for (int i = 0; i < elevationList.size(); i += 100) {
            var points = new ArrayList<Point>();
            var latlon = new ArrayList<Double>();

            for (int j = 0; j < 100 && i + j < elevationList.size(); j++) {
                points.add(elevationList.get(i + j));
                latlon.add(elevationPoints.get((i + j) * 2));
                latlon.add(elevationPoints.get((i + j) * 2 + 1));
            }

            var fails = 0;

            while (true)
            {
                try {
                    var batchElevations = OpenElevation.getElevation(latlon);
    
                    for (int j = 0; j < points.size(); j++) {
                        elevationGrid.put(points.get(j), batchElevations[j]);
                    }
                    
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Downloaded elevations for " + (i + 100) + " / " + elevationList.size() + " nodes");

                    break;
                } catch (IOException ei) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    fails++;

                    System.out.println("Failed to download elevations for " + elevationList.size() + " nodes, retrying (" + fails + ")");

                    if (fails > 10) {
                        throw ei;
                    }
                }
            }
        }

        var elevations = new HashMap<Long, Double>();

        for (var i = 0; i < newNodes.size(); i++) {
            var node = nodes.get(newNodes.get(i));
            var lat = newPoints.get(i * 2);
            var lon = newPoints.get(i * 2 + 1);
            
            // Round to 3 decimal places
            var point = new Point((int) (lat * 1000), (int) (lon * 1000));

            elevations.put(node.id, elevationGrid.get(point));
        }

        graph.processElevations(elevations);

        // Add relations
        for (Relation relation : osm.relation) {
            if (relations.containsKey(relation.id)) continue;

            relations.put(relation.id, relation);

            graph.processRelation(relation);
        }
    }

    /**
     * Download data from Open Street Map, and store it in the database.
     * Center the download area on the given coordinates.
     * 
     * @param point                     Point to center the download area on
     */
    public void downloadData(Point point) throws IOException {
        // Check if the point is within the bounds
        if (bounds != null) {
            if (point.getLatitude()  >= bounds.minlat && point.getLatitude()  <= bounds.maxlat && 
                point.getLongitude() >= bounds.minlon && point.getLongitude() <= bounds.maxlon) {
                return;
            }
        }

        var bottom = point.move(-DOWNLOAD_SIZE / 2, -DOWNLOAD_SIZE / 2);
        var top = point.move(DOWNLOAD_SIZE / 2, DOWNLOAD_SIZE / 2);

        downloadData(
            bottom.getLatitude(), bottom.getLongitude(),
            top.getLatitude(), top.getLongitude()
        );
    }

    /**
     * Add OSM data to the database
     * 
     * @param osm                       OSM data
     */
    public void addData(Osm osm) {
        // Add nodes
        for (Node node : osm.node) {
            nodes.put(node.id, node);
        }

        // Add ways
        for (Way way : osm.way) {
            ways.put(way.id, way);
        }

        // Add relations
        for (Relation relation : osm.relation) {
            relations.put(relation.id, relation);
        }
    }

    /**
     * Get a node from the database
     * 
     * @param id                        Node ID
     * @return                          Node
     * @throws IllegalArgumentException If the node does not exist
     */
    public Node getNode(long id) throws IllegalArgumentException {
        var node = nodes.get(id);

        if (node == null) {
            throw new IllegalArgumentException("Node " + id + " does not exist");
        }

        return node;
    }

    /**
     * Get a way from the database
     * 
     * @param id                        Way ID
     * @return                          Way
     * @throws IllegalArgumentException If the way does not exist
     */
    public Way getWay(long id) throws IllegalArgumentException {
        var way = ways.get(id);

        if (way == null) {
            throw new IllegalArgumentException("Way " + id + " does not exist");
        }

        return way;
    }

    /**
     * Get a relation from the database
     * 
     * @param id                        Relation ID
     * @return                          Relation
     * @throws IllegalArgumentException If the relation does not exist
     */
    public Relation getRelation(long id) throws IllegalArgumentException {
        var relation = relations.get(id);

        if (relation == null) {
            throw new IllegalArgumentException("Relation " + id + " does not exist");
        }

        return relation;
    }

    /**
     * Query for ways that contain a key with one of a set of values
     *
     * @param key                       Key
     * @param values                    Values
     * @return                          Ways
     */
    public Way[] queryWays(String key, String... values) {
        // Create a list of ways
        ArrayList<Way> ways = new ArrayList<Way>();

        // Loop through all ways
        for (Way way : getWays().values()) {

            if (way.tag == null) continue;

            // Loop through all tags
            for (Tag tag : way.tag) {

                // Check if the key matches
                if (!tag.k.equals(key)) continue;

                // Loop through all values
                for (String value : values) {

                    // Check if the value matches
                    if (!tag.v.equals(value)) continue;

                    // Add the way to the list
                    ways.add(way);

                    break;
                }
            }
        }

        // Convert the list to an array
        Way[] waysArray = new Way[ways.size()];
        ways.toArray(waysArray);

        return waysArray;
    }

    /**
     * Query for relations that contain a key with one of a set of values
     * 
     * @param key                       Key
     * @param values                    Value
     */
    public Relation[] queryRelations(String key, String... values) {
        // Create a list of relations
        ArrayList<Relation> relations = new ArrayList<Relation>();

        // Loop through all relations
        for (Relation relation : getRelations().values()) {

            if (relation.tag == null) continue;

            // Loop through all tags
            for (Tag tag : relation.tag) {

                // Check if the key matches
                if (!tag.k.equals(key)) continue;

                // Loop through all values
                for (String value : values) {

                    // Check if the value matches
                    if (!tag.v.equals(value)) continue;

                    // Add the relation to the list
                    relations.add(relation);

                    break;
                }
            }
        }

        // Convert the list to an array
        Relation[] relationsArray = new Relation[relations.size()];
        relations.toArray(relationsArray);

        return relationsArray;
    }

    /**
     * Get all nodes that are part of a way
     * 
     * @param way                       Way
     */
    public Node[] getNodes(Way way) {
        // Create a list of nodes
        ArrayList<Node> nodes = new ArrayList<Node>(way.nd.length);

        // Loop through all way nodes
        for (Nd wayNode : way.nd) {

            // Get the node
            Node node = getNode(wayNode.ref);

            // Add the node to the list
            nodes.add(node);
        }

        // Convert the list to an array
        Node[] nodesArray = new Node[nodes.size()];
        nodes.toArray(nodesArray);

        return nodesArray;
    }

    /**
     * Get all nodes that are part of a relation
     * 
     * @param relation                  Relation
     */
    public Node[] getNodes(Relation relation) {
        // Create a list of nodes
        ArrayList<Node> nodes = new ArrayList<Node>();

        // Loop through all relation members
        for (Member member : relation.member) {

            if (member.type.equals("way")) {
                if (!ways.containsKey(member.ref)) continue;
                
                nodes.addAll(Arrays.asList(getNodes(getWay(member.ref))));

                continue;
            }
            // Get the node

            //nodes.add(getNode(member.ref));
        }

        // Convert the list to an array
        Node[] nodesArray = new Node[nodes.size()];
        nodes.toArray(nodesArray);

        return nodesArray;
    }

    /**
     * Get value of a tag
     * 
     * @param tags                      Tags
     */
    public String getTagValue(Tag[] tags, String key) {
        if (tags == null) return null;

        // Loop through all tags
        for (Tag tag : tags) {

            // Check if the key matches
            if (!tag.k.equals(key)) continue;

            // Return the value
            return tag.v;
        }

        return null;
    }


    /** Used to find all tag values for an osm node, way or relation
     * @param tags - the tags to get the tag values from
     * @return - a set of all values found inside the tags
     */
    public HashMap<String,String> getTagValues(Tag[] tags){
        HashMap<String,String> result = new HashMap<>();
        for(Tag tag:tags){
            result.put(tag.k,tag.v);
        }
        return result;
    }
}
