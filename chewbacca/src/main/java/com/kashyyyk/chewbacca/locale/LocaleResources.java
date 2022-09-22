package com.kashyyyk.chewbacca.locale;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Locale resources
 */
public class LocaleResources {
    /**
     * Name of the English locale
     */
    public static final String ENGLISH = "english";

    /**
     * Name of the Swedish locale
     */
    public static final String SWEDISH = "swedish";

    /**
     * Name of the Spanish locale
     */
    public static final String SPANISH = "spanish";

    /**
     * Array of supported locales
     */
    public static final String[] SUPPORTED_LOCALES = {ENGLISH, SWEDISH, SPANISH};

    /**
     * The English locale
     */
    private Locales english;

    /**
     * The Swedish locale
     */
    private Locales swedish;

    /**
     * The Spanish locale
     */
    private Locales spanish;

    /**
     * The singleton instance
     */
    private static LocaleResources instance;

    /**
     * Load the locales from the static resources
     */
    public void loadFromResources() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        
        // Load the files from the static resources at static/locale as json files
        Resource resource = new ClassPathResource("locales/english.json");
        
        InputStream inputStream = resource.getInputStream();
        english = mapper.readValue(inputStream, Locales.class);

        resource = new ClassPathResource("locales/swedish.json");
        inputStream = resource.getInputStream();
        swedish = mapper.readValue(inputStream, Locales.class);

        resource = new ClassPathResource("locales/spanish.json");
        inputStream = resource.getInputStream();
        spanish = mapper.readValue(inputStream, Locales.class);
    }

    /**
     * Get the singleton instance
     * 
     * @return The singleton instance
     */
    public static LocaleResources getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new LocaleResources();
            instance.loadFromResources();
        }

        return instance;
    }

    /**
     * Get the locales for a specific language
     * 
     * @param language  The language to get the locales for
     * @return          The locales for the language
     */
    public static Locales getLocales(String language) throws IOException
    {
        getInstance();

        if (language.equals(ENGLISH))
        {
            return instance.english;
        }
        else if (language.equals(SWEDISH))
        {
            return instance.swedish;
        }
        else if (language.equals(SPANISH))
        {
            return instance.spanish;
        }

        throw new IOException("Language not supported");
    }
}
