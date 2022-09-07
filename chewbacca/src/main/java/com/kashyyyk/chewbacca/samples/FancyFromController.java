package com.kashyyyk.chewbacca.samples;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * See FormController.java for more information.
 */
@Controller
public class FancyFromController {

    /**
     * Render the 'samples/fancy-form.html' page.
     * 
     * @param model     the model to add data to
     * @return          the model and view to render
     */
    @GetMapping("samples/fancy-greeting")
    public ModelAndView greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());

        return new ModelAndView("samples/fancy-form.html", model.asMap());
    }

    /**
     * A POST handler for the 'samples/fancy-form.html' page.
     * 
     * @param greeting  the Greeting object to populate with data from the form
     * @param model     the model to add data to
     * @return          the model and view to render
     */
    @PostMapping("samples/fancy-greeting")
    public ModelAndView greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        model.addAttribute("greeting", greeting);

        return new ModelAndView("samples/fancy-form-result.html", model.asMap());
    }
}
