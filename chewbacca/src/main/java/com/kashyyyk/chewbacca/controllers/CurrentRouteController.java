package com.kashyyyk.chewbacca.controllers;

import com.kashyyyk.chewbacca.locale.LocaleResources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class CurrentRouteController {

    @GetMapping("current-route")
    public ModelAndView getIndex(Model model, @CookieValue(value = "lang", defaultValue = "english") String language) {

        try {
            LocaleResources.getLocales(language).populateModel(model);
        } catch (IOException e) {
            return new ModelAndView("html_pages/error.html", model.asMap());
        }

        return new ModelAndView("html_pages/CurrentRoute.html", model.asMap());
    }

}
