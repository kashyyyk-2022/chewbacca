package com.kashyyyk.chewbacca.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SettingsController {

    @GetMapping("settings")
    public ModelAndView getSettings(Model model, @CookieValue(value = "unit", defaultValue = "metric") String unit) {
        model.addAttribute("unit", unit);

        return new ModelAndView("html_pages/settings.html", model.asMap());
    }
    
}
