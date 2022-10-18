package com.kashyyyk.chewbacca.locale;

import java.util.HashMap;

import org.springframework.ui.Model;

/**
 * A locale set.
 * 
 * Contains the language and a map of phrases.
 */
public class Locales {
    /**
     * Language of the locale.
     */
    public String language;

    /**
     * Map of phrases.
     */
    public HashMap<String, String> phrases;

    /**
     * Constructor.
     */
    public Locales() {
        phrases = new HashMap<String, String>();
    }

    /**
     * Get the language.
     * 
     * @return language
     */
    public void getLanguage(String language) {
        this.language = language;
    }

    /**
     * Get a phrase.
     * 
     * @param key   key of the phrase
     * @return      phrase
     */
    public void getPhrase(String key) {
        phrases.get(key);
    }

    /**
     * Populate a model with the locale.
     * 
     * @param model model to populate
     */
    public void populateModel(Model model) {
        model.addAttribute("language", language);
        
        for (String key : phrases.keySet()) {
            model.addAttribute(key, phrases.get(key));
        }
    }
}
