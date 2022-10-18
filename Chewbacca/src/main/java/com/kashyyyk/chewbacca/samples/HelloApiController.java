package com.kashyyyk.chewbacca.samples;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Notice that this class is annotated with @RestController. This means that
 * the methods in this class will return data directly to the caller, rather
 * than a view to render.
 */
@RestController
public class HelloApiController {
    public int id;

    /**
     * This is a sample controller that returns a JSON object.
     * 
     * @param name      the name attribute
     * @return          the ApiData object
     */
    @GetMapping("samples/helloapi")
    public ApiData getHelloApi(
        /*
         * The @RequestParam annotation means that the 'name' parameter will be
         * passed in the query string, e.g. http://localhost:8080/helloapi?name=World
         * We can specify if the parameter is required or not, and a default value.
         */
        @RequestParam(name="name", required=false, defaultValue="World")
        String name
    ) {
        // Create the object to return
        var apiData = new ApiData();

        apiData.name = name;
        
        apiData.id = id;

        // Increment the id
        id++;

        // Return the object
        return apiData;
    }
}
