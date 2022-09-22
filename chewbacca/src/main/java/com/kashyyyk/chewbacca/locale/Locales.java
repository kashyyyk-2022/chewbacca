package com.kashyyyk.chewbacca.locale;

import java.util.HashMap;

import org.springframework.ui.Model;

public class Locales {
    public String language;

    public HashMap<String, String> phrases;

    public Locales() {
        phrases = new HashMap<String, String>();
    }

    public void getPhrase(String key) {
        phrases.get(key);
    }

    public void populateModel(Model model) {
        model.addAttribute("language", language);
        
        for (String key : phrases.keySet()) {
            model.addAttribute(key, phrases.get(key));
        }
    }
}
