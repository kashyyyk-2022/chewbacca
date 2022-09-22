package com.kashyyyk.chewbacca.controllers;

import com.kashyyyk.chewbacca.locale.LocaleResources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class IndexController {

    @GetMapping("")
    public ModelAndView getIndex(
            Model model,
            @CookieValue(value = "unit", defaultValue = "metric") String unit,
            @CookieValue(value = "lang", defaultValue = "en") String lang,
            @CookieValue(value = "darkMode", defaultValue = "false") String darkMode
    ){
        model.addAttribute("unit", unit);
        model.addAttribute("lang", lang);
        model.addAttribute("darkMode", darkMode);

        return new ModelAndView("html_pages/index.html", model.asMap());
    }
}
