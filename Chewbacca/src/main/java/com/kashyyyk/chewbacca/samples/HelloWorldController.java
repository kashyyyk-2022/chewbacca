package com.kashyyyk.chewbacca.samples;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

    /**
     * This is a controller which utilizes Thymeleaf to render a dynamic page.
     * 
     * @param model     the model to add data to
     * @return          the model and view to render
     */
    @GetMapping("samples/hello")
    public ModelAndView getHello(Model model) {
        /*
         * Set the 'name' attribute of the model to 'World'.
         */
        model.addAttribute("name", "World");

        /*
         * Return a new ModelAndView with the name of the page to render
         * and the model to use. Uses model.asMap() to get the model as a Map.
         */
        return new ModelAndView("samples/hello.html", model.asMap());
    }
}
