package com.kashyyyk.chewbacca.controllers;

import com.kashyyyk.chewbacca.controllers.structures.RouteAPIDoneResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoutingAPIController {

    public static int i = 1;

    @GetMapping("route-api/done")
    public RouteAPIDoneResponse getDone(
            @RequestParam(name = "id", required = true) String id
    ){
        i++;
        if(i > 10){
            return new RouteAPIDoneResponse(id, true);
        }
        return new RouteAPIDoneResponse(id, false);

    }


}
