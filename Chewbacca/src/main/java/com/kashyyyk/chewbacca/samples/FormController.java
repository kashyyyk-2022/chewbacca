package com.kashyyyk.chewbacca.samples;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FormController {

    /**
     * Render the 'samples/form.html' page.
     * 
     * @param model     the model to add data to
     * @return          the model and view to render
     */
    @GetMapping("samples/greeting")
    public ModelAndView greetingForm(Model model) {
        /*
         * Set the 'greeting' attribute of the model to a new Greeting object.
         * This will allow the form to populate the Greeting object with data
         * to be sent back with a POST request.
         */
        model.addAttribute("greeting", new Greeting());

        return new ModelAndView("samples/form.html", model.asMap());
    }

    /**
     * A POST handler for the 'samples/form.html' page.
     * This request will include data from the form in the form of a Greeting object;
     * we will get back the same Greeting object we created in the GET handler, but
     * populated with data from the form.
     * 
     * @param greeting  the Greeting object to populate with data from the form
     * @param model     the model to add data to
     * @return          the model and view to render
     */
    @PostMapping("samples/greeting")
    public ModelAndView greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        /*
         * Now the server has received the data from the form, we can do something with it.
         * 
         * Here we simply add the Greeting object back to the model.
         */
        model.addAttribute("greeting", greeting);

        /*
         * Render the samples/form-result.html page. With the greeting object we got from
         * the client in the model.
         */
        return new ModelAndView("samples/form-result.html", model.asMap());
    }
}
