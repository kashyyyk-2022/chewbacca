package com.kashyyyk.chewbacca.controllers;

import com.kashyyyk.chewbacca.controllers.structures.RouteAPIContentResponse;
import com.kashyyyk.chewbacca.controllers.structures.RouteAPIDoneResponse;
import com.kashyyyk.chewbacca.controllers.structures.RouteAPIStartResponse;
import com.kashyyyk.chewbacca.services.LocalRoutingStorage;
import com.kashyyyk.chewbacca.services.RoutingService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingAPIController {
    /**
     * The routing service
     */
    private static final RoutingService routingService = new RoutingService(
        new LocalRoutingStorage(),
        4
    );

    /**
     * Get the routing service
     * 
     * @return the routing service
     */
    public static RoutingService getRoutingService() {
        return routingService;
    }

    @GetMapping("route-api/done")
    public RouteAPIDoneResponse getDone(
            @RequestParam(name = "id", required = true) String id
    ) {
        return new RouteAPIDoneResponse(id, routingService.isRouteDone(id));
    }

    @GetMapping("route-api/start")
    public RouteAPIStartResponse getRoute(
            @RequestParam(name = "lat", required = true) String lat,
            @RequestParam(name = "lon", required = true) String lon,
            @RequestParam(name = "hours", required = true) String hours,
            @RequestParam(name = "minutes", required = true) String minutes,
            @RequestParam(name = "distance", required = true) String distance,
            @RequestParam(name = "elevation", required = true) String elevation,
            @RequestParam(name = "terrain", required = true) String terrain
    ) {
        return new RouteAPIStartResponse(routingService.generateRoute(
            Double.parseDouble(lat),
            Double.parseDouble(lon),
            Double.parseDouble(hours),
            Double.parseDouble(minutes),
            Double.parseDouble(distance),
            Double.parseDouble(elevation),
            Integer.parseInt(terrain)
        ));
    }

    @GetMapping("route-api/content")
    public RouteAPIContentResponse getContent(
            @RequestParam(name = "id", required = true) String id
    ) {
        return new RouteAPIContentResponse(
            id,
            routingService.isRouteDone(id),
            routingService.getRoute(id)
        );
    }

}
