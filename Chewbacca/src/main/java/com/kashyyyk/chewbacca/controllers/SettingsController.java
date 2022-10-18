package com.kashyyyk.chewbacca.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kashyyyk.chewbacca.locale.LocaleResources;

@Controller
public class SettingsController {

    @GetMapping("settings")
    public ModelAndView getSettings(
        Model model,
        @CookieValue(value = "unit", defaultValue = "metric") String unit,
        @CookieValue(value = "lang", defaultValue = "english") String language    
    ) {
        model.addAttribute("unit", unit);
        model.addAttribute("lang", language);

        try {
            LocaleResources.getLocales(language).populateModel(model);
        } catch (IOException e) {
            return new ModelAndView("html_pages/error.html", model.asMap());
        }

        return new ModelAndView("html_pages/settings.html", model.asMap());
    }

}
