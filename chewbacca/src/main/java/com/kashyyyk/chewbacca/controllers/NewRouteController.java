package com.kashyyyk.chewbacca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewRouteController {
    
    @GetMapping("newroute")
    public ModelAndView getIndex(Model model) {
        return new ModelAndView("html_pages/newroute.html", model.asMap());
    }

}