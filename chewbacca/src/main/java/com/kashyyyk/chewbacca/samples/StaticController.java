package com.kashyyyk.chewbacca.samples;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    /**
     * This is a sample controller that returns a static page.
     * 
     * The name of this method is unimportant, can be named anything.
     * 
     * The @GetMapping('/static') means this will be called when a GET request is
     * made to the /static endpoint, e.g. http://localhost:8080/static
     * 
     * @return      the name of the static page to return
     */
    @GetMapping("samples/static")
    public String getStatic() {
        /*
         * When the web page does not need any data, having the return type
         * as a String and returning the name of the page is sufficient.
         * 
         * This returns the web page located at
         * src/main/resources/templates/samples/static.html
         * i.e 'samples/static.html' relative to the templates directory.
         */
        return "stamples/static.html";
    }
}