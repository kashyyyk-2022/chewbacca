package com.kashyyyk.chewbacca.controllers;

import com.kashyyyk.chewbacca.controllers.structures.RouteAPIContentResponse;
import com.kashyyyk.chewbacca.controllers.structures.RouteAPIDoneResponse;
import com.kashyyyk.chewbacca.controllers.structures.RouteAPIStartResponse;
import com.kashyyyk.chewbacca.services.LocalRoutingStorage;
import com.kashyyyk.chewbacca.services.RoutingService;

import org.springframework.http.MediaType;
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

    @GetMapping(path = "route-api/done", produces = MediaType.APPLICATION_JSON_VALUE)
    public RouteAPIDoneResponse getDone(
            @RequestParam(name = "id", required = true) String id
    ) {
        return new RouteAPIDoneResponse(id, routingService.isRouteDone(id));
    }

    @GetMapping(path = "route-api/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public RouteAPIStartResponse getRoute(
            @RequestParam(name = "lat", required = true) String lat,
            @RequestParam(name = "lon", required = true) String lon,
            @RequestParam(name = "hours", required = true) String hours,
            @RequestParam(name = "minutes", required = true) String minutes,
            @RequestParam(name = "distance", required = true) String distance,
            @RequestParam(name = "elevation", required = true) String elevation,
            @RequestParam(name = "terrain", required = true) String terrain
    ) {
        double convertedElevation = 10;
        switch(elevation){
            case "low": convertedElevation = 10; break;
            case "medium": convertedElevation = 50; break;
            case "high": convertedElevation = 100; break;
        }

        return new RouteAPIStartResponse(routingService.generateRoute(
            Double.parseDouble(lat),
            Double.parseDouble(lon),
            Double.parseDouble(distance),
            Double.parseDouble(hours),
            Double.parseDouble(minutes),
            convertedElevation,
            terrain
        ));
    }

    @GetMapping(path = "route-api/content", produces = MediaType.APPLICATION_JSON_VALUE)
    public RouteAPIContentResponse getContent(
            @RequestParam(name = "id", required = true) String id
    ) {
        return new RouteAPIContentResponse(
            id,
            routingService.isRouteDone(id),
            routingService.getRoute(id),
            routingService.getRouteStart(id),
            routingService.getRouteEnd(id),
            routingService.getRouteLabels(id)
        );
    }

}
