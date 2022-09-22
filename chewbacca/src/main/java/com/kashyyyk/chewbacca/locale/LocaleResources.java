package com.kashyyyk.chewbacca.locale;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LocaleResources {    
    public static final String ENGLISH = "english";
    public static final String SWEDISH = "swedish";
    public static final String SPANISH = "spanish";

    public static final String[] SUPPORTED_LOCALES = {ENGLISH, SWEDISH, SPANISH};

    private Locales english;

    private Locales swedish;

    private Locales spanish;

    private static LocaleResources instance;

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

    public static LocaleResources getInstance() throws IOException
    {
        if (instance == null)
        {
            instance = new LocaleResources();
            instance.loadFromResources();
        }

        return instance;
    }

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
