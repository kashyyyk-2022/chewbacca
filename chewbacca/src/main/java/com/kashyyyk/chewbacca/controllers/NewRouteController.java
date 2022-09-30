package com.kashyyyk.chewbacca.controllers;

import com.kashyyyk.chewbacca.locale.LocaleResources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class NewRouteController {
    @GetMapping("newroute")
    public ModelAndView getIndex(Model model,
                                 @CookieValue(value = "unit", defaultValue = "metric") String unit,
                                 @CookieValue(value = "lang", defaultValue = "english") String lang,
                                 @CookieValue(value = "darkMode", defaultValue = "false") String darkMode,
                                 @CookieValue(value = "hours", defaultValue = "0") String hours,
                                 @CookieValue(value = "minutes", defaultValue = "0") String minutes,
                                 @CookieValue(value = "distance", defaultValue = "0") String distance,
                                 @CookieValue(value = "elevation", defaultValue = "0") String elevation)
    {

        model.addAttribute("unit", unit);
        model.addAttribute("lang", lang);
        model.addAttribute("darkMode", darkMode);
        model.addAttribute("hours", hours);
        model.addAttribute("minutes", minutes);
        model.addAttribute("distance", distance);
        model.addAttribute("elevation", elevation);

        try {
            LocaleResources.getLocales(lang).populateModel(model);
        } catch (IOException e) {
            return new ModelAndView("html_pages/newroute.html", model.asMap());
        }

        return new ModelAndView("html_pages/newroute.html", model.asMap());

    }

}