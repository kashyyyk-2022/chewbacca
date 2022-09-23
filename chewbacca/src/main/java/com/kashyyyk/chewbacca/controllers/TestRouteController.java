package com.kashyyyk.chewbacca.controllers;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.catalina.connector.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestRouteController {

    @GetMapping("testroute")
    public ModelAndView getIndex(Model model) {
        return new ModelAndView("html_pages/testroute.html", model.asMap());
    }


    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
            .url("https://graphhopper.com/api/1/route?point=51.131,12.414&point=48.224,3.867&profile=car&locale=de&calc_points=false&key=api_key")
            .get()
            .build();

    Response response = client.newCall(request).execute();
}
