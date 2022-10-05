package com.kashyyyk.chewbacca.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalRoutingStorage implements RoutingStorage {

    private Map<String, LocalRoute> routes = new HashMap<>();

    @Override
    public String newRoute() {
        String id = UUID.randomUUID().toString();
        routes.put(id, new LocalRoute(id, false, null));
        return id;
    }

    @Override
    public double[][] getRoute(String id) {
        return routes.get(id).getRoute();
    }

    @Override
    public void setRoute(String id, double[][] route) {
        routes.get(id).setRoute(route);
    }

    @Override
    public void deleteRoute(String id) {
        routes.remove(id);
    }

    @Override
    public boolean isRouteDone(String id) {
        return routes.get(id).isDone();
    }

    @Override
    public void setRouteDone(String id, boolean done) {
        routes.get(id).setDone(done);
    }
    
}
