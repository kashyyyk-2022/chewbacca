package com.kashyyyk.chewbacca.map;

import static com.kashyyyk.chewbacca.map.Osm.*;

public interface OsmGraph {

    /**
     * Set the database
     * 
     * @param database                  The database
     */
    public void setDatabase(OsmDatabase database);

    /**
     * Process a new node
     * 
     * @param node                      The node to process
     */
    public void processNode(Node node);

    /**
     * Process a new way
     * 
     * @param way                       The way to process
     */
    public void processWay(Way way);

    /**
     * Process a new relation
     * 
     * @param relation                  The relation to process
     */
    public void processRelation(Relation relation);

}
